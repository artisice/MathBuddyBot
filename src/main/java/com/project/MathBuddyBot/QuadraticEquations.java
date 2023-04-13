package com.project.MathBuddyBot;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuadraticEquations {
    public static void getQuad(Model model) throws IOException {

        URL url = new URL("https://shadify.dev/api/math/quad/?max-a=2&max-b=9&max-c=3&min-c=-9&negative=0");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setExpression(object.getString("equation"));
        List<Double> list = new ArrayList<>();
        list.add(Double.valueOf(object.getString("x1")));
        list.add(Double.valueOf(object.getString("x2")));
        model.setRoots(list);
    }
}
