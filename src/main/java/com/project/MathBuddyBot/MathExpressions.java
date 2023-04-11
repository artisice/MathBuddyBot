package com.project.MathBuddyBot;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class MathExpressions {

    public static void getInfo(Model model) throws IOException {
        List<String> operatorList = new ArrayList<>();
        operatorList.add("add");
        operatorList.add("sub");
        operatorList.add("mul");
        operatorList.add("div");

        String operator = operatorList.get(new Random().nextInt(operatorList.size()));
        String parameter = String.valueOf(new Random().nextInt(2));

        URL url = new URL("https://shadify.dev/api/math/" + operator + "/?negative=" + parameter);

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setExpression(object.getString("expression"));
        model.setAnswer(object.getInt("answer"));

    }

}
