package com.ailu.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * @Description: 计算工具
 * @Author: ailu
 * @Date: 2024/10/26 下午4:36
 */

public class ComputingTool {

    @Tool("计算两个数的总和")
    public double sum(double a, double b) {
        return a + b;
    }

    @Tool("计算两个数的乘积")
    public double multiply(double a, double b) {
        return a * b;
    }

    @Tool("计算两个数的差")
    public double difference(double a, double b) {
        return a - b;
    }

    @Tool("计算两个数的商")
    public double quotient(@P("被除数") double a, @P("除数") double b) {
        return a / b;
    }

    @Tool("计算两个数的平均值")
    public double average(double a, double b) {
        return (a + b) / 2;
    }

    @Tool("计算两个数的余数")
    public double remainder(@P("被除数") double a, @P("除数") double b) {
        return a % b;
    }

    @Tool("计算一个数的开方")
    public double squareRoot(double x) {
        return Math.sqrt(x);
    }

}
