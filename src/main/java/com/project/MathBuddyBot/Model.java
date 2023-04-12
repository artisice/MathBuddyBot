package com.project.MathBuddyBot;

import lombok.Data;

import java.util.List;

@Data
public class Model {

    String expression;
    Integer answer;
    List<Double> roots;

}
