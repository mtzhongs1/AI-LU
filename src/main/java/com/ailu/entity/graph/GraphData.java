package com.ailu.entity.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphData {

    // Getters and Setters
    @JsonProperty("nodes")
    private List<String> nodes;

    @JsonProperty("relations")
    private List<Relation> relations;

    @Data
    public static class Relation {
        @JsonProperty("subject")
        private String subject;

        @JsonProperty("relation")
        private String relation;

        @JsonProperty("object")
        private String object;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Relation relation1 = (Relation) o;
            return Objects.equals(subject, relation1.subject) && Objects.equals(relation, relation1.relation) && Objects.equals(object, relation1.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, relation, object);
        }

        // Getters and Setters
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }
    }
}
