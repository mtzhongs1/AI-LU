package com.ailu.service.other.Impl;

import com.ailu.entity.graph.GraphData;
import com.ailu.entity.graph.NodeAndEdge;
import com.ailu.executor.ThreadPoolExecutorFactory;
import com.ailu.service.aiServices.ExtractEntityAndRelationServices;
import com.ailu.service.other.KnowledgeBaseService;
import com.ailu.util.UuidUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.graph.neo4j.Neo4jGraph;
import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static org.neo4j.driver.Values.parameters;

/**
 * @Description: 知识库相关操作
 * @Author: ailu
 * @Date: 2024/10/26 下午12:41
 */
@Service
@Slf4j
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    public static final String[] POI_DOC_TYPES = {"doc", "docx", "ppt", "pptx", "xls", "xlsx"};

    @Autowired
    private EmbeddingModel embeddedModel;

    @Resource(name = "inMemoryEmbeddingStore")
    private EmbeddingStore<TextSegment> inMemoryEmbeddingStore;

    @Resource(name = "neo4jEmbeddingStore")
    private EmbeddingStore<TextSegment> neo4jEmbeddingStore;

    @Autowired
    private ExtractEntityAndRelationServices extractEntityAndRelationServices;

    @Override
    public void uploadKnowledgeBase(MultipartFile file) {
        //保存文档到本地
        String originalFilename = file.getOriginalFilename();
        String uuid = UuidUtils.getUUID();
        log.info("上传文档的UUID为：{}",uuid);
        String fileName = StringUtils.cleanPath(originalFilename);
        String ext = getFileExtension(fileName);
        String pathName = "D:/RAG/" + uuid + "." + ext;

        try {
            file.transferTo(new File(pathName));
        } catch (IOException e) {
            log.error("save to local error", e);
        }

        //转化文档格式
        Document doc = transferExt(ext, pathName);
        //使用InMemoryEmbeddingStore嵌入
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()

                // 使用指定的 DocumentTransformer 转换文档。
                .documentTransformer(document -> {
                    //放置元数据uuid，进行标识
                    document.metadata().put("uuid", uuid);
                    return document;
                })

                // 使用指定的 DocumentSplitter 将文档拆分为 TextSegments
                .documentSplitter(DocumentSplitters.recursive(1000, 200, new OpenAiTokenizer()))
                .embeddingModel(embeddedModel)
                .embeddingStore(inMemoryEmbeddingStore)
                .build();
        ingestor.ingest(doc);
    }

    @Override
    public void uploadKnowledgeGraph(MultipartFile file) {

        //保存文档到本地
        String originalFilename = file.getOriginalFilename();
        String uuid = UuidUtils.getUUID();
        log.info("上传文档的UUID为：{}",uuid);
        String fileName = StringUtils.cleanPath(originalFilename);
        String ext = getFileExtension(fileName);
        String pathName = "D:/RAG/" + uuid + "." + ext;

        try {
            file.transferTo(new File(pathName));
        } catch (IOException e) {
            log.error("save to local error", e);
        }

        //转化文档格式
        Document doc = transferExt(ext, pathName);

        // 句号分割器
        DocumentSplitter documentSplitter = new DocumentByRegexSplitter("(?<=\\.|\\。|\\n)\\s*"," ",200,100);
        // DocumentSplitter documentSplitter = buildDocumentSplitter();
        List<TextSegment> textSegments = documentSplitter.split(doc);

        try(Driver driver = GraphDatabase.driver("bolt://localhost:7687/"
                , AuthTokens.basic("neo4j", "dfDF51d5"));
            Session session = driver.session()
        ){
            Set<String> nodes = new HashSet<>();
            Set<GraphData.Relation> relations = new HashSet<>();
            for (TextSegment textSegment : textSegments) {
                //提取实体和关系
                String nodeAndData = extractEntityAndRelationServices.extractNodeAndRelation(textSegment.text());

                String jsonStr = extractJson(nodeAndData);
                System.out.println(jsonStr);
                ObjectMapper objectMapper = new ObjectMapper();
                GraphData graphData = objectMapper.readValue(jsonStr, GraphData.class);
                nodes.addAll(graphData.getNodes());
                relations.addAll(graphData.getRelations());
            }
            session.writeTransaction(tx -> {
                for (String node : nodes) {
                    String query = String.format("CREATE (u:`%s`)", node);
                    tx.run(query);
                }
                for (GraphData.Relation relation : relations) {
                    String query = String.format(
                            "MERGE (a:`%s` {name: $subjectName}) " +
                                    "MERGE (b:`%s` {name: $objectName}) " +
                                    "MERGE (a)-[r:`%s`]->(b)",
                            relation.getSubject(),
                            relation.getObject(),
                            relation.getRelation()
                    );
                    tx.run(query, org.neo4j.driver.Values.parameters(
                            "subjectName", relation.getSubject(),
                            "objectName", relation.getObject()
                    ));
                }
                return null;
            });
        } catch (Exception e) {
            log.error("save to neo4j error: ", e);
        }


        // //使用InMemoryEmbeddingStore嵌入
        // EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
        //
        //         // 使用指定的 DocumentTransformer 转换文档。
        //         .documentTransformer(document -> {
        //             //放置元数据uuid，进行标识
        //             document.metadata().put("uuid", uuid);
        //             return Document.document(nodeAndData, document.metadata());
        //         })
        //
        //         // 使用指定的 DocumentSplitter 将文档拆分为 TextSegments
        //         .documentSplitter(documentSplitter)
        //         .embeddingModel(embeddedModel)
        //         .embeddingStore(neo4jEmbeddingStore)
        //         .build();
        // ingestor.ingest(doc);
    }

    //提取最外层的json字符串
    private String extractJson(String nodeAndData) {
        int first = nodeAndData.indexOf("{");
        int last = nodeAndData.lastIndexOf("}");
        return first >= 0 && last >= 0 ? nodeAndData.substring(first, last + 1) : nodeAndData;
    }

    // private List<String> extractNodeAndRelation(DocumentSplitter documentSplitter, Document doc) {
    //     List<String> nodeAndEdges = new ArrayList<>();
    //     List<TextSegment> textSegments = documentSplitter.split(doc);
    //     for (TextSegment textSegment : textSegments) {
    //         nodeAndEdges.add(extractEntityAndRelationServices.extractNodeAndRelation(textSegment.text()));
    //     }
    //     return nodeAndEdges;
    // }

    //构建文档分割器
    private static DocumentSplitter buildDocumentSplitter() {
        return DocumentSplitters.recursive(500, 200, new OpenAiTokenizer());
    }

    //获取文件后缀
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        } else {
            return fileName.substring(dotIndex + 1);
        }
    }
    //转换文件格式
    private static Document transferExt(String ext, String pathName) {
        Document document;
        if (ext.equalsIgnoreCase("txt")) {
            document = loadDocument(pathName, new TextDocumentParser());
        } else if (ext.equalsIgnoreCase("pdf")) {
            document = loadDocument(pathName, new ApachePdfBoxDocumentParser());
        } else if (ArrayUtils.contains(POI_DOC_TYPES, ext)) {
            document = loadDocument(pathName, new ApachePoiDocumentParser());
        } else {
            log.warn("该文件类型:{}无法解析，忽略", ext);
            throw new RuntimeException("该文件类型无法解析");
        }
        return document;
    }
}
