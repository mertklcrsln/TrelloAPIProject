package org.example.coreLayer.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Trello {

    private static final String API_KEY = "d53a748eb4eecec2b4545a109eba4e69";
    private static final String TOKEN = "ATTAa91d872ea974f94e677c89819251a5e956c36a769750bb842c9cb69cb2ad52003A9BEEE1";
    private String boardId;
    private String listId;
    private String firstCardId;
    private String secondCardId;
    private String newBoardName;
    private String newListName1;
    private String newListName2;
    private String newCardName1;
    private String newCardName2;
    private String updatedListName;


    @Test
    public void createBoard() {

        newBoardName = "new_boardDene";
        JSONObject expectedBody = new JSONObject();
        expectedBody.put("name", newBoardName);

        Response response = RestAssured.given().baseUri("https://api.trello.com/1/boards/").
                queryParam("name",newBoardName).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                post();

        response.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody = response.jsonPath();
        Assert.assertEquals(expectedBody.get("name"), actualBody.get("name"));

        // Dönen verileri al
        String responseBody = response.prettyPrint();
        System.out.println("Response Body: " + responseBody);

        // Dönen verilerden id'yi al
        boardId = response.jsonPath().getString("id");
        System.out.println("Board ID: " + boardId);

    }

    @Test(dependsOnMethods = "createBoard")
    public void createLists() {
        newListName1 = "new_List1";
        newListName2 = "new_List2";

        JSONObject expectedBody1 = new JSONObject();
        expectedBody1.put("name", newListName1);

        JSONObject expectedBody2 = new JSONObject();
        expectedBody2.put("name", newListName2);

        Response response1 = RestAssured.given().baseUri("https://api.trello.com/1/lists").
                queryParam("name",newListName1).
                queryParam("idBoard",boardId).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                post();

        Response response2 = RestAssured.given().baseUri("https://api.trello.com/1/lists").
                queryParam("name",newListName2).
                queryParam("idBoard",boardId).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                post();


        response1.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody1 = response1.jsonPath();
        Assert.assertEquals(expectedBody1.get("name"), actualBody1.get("name"));

        response2.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody2 = response2.jsonPath();
        Assert.assertEquals(expectedBody2.get("name"), actualBody2.get("name"));

        listId = response1.jsonPath().getString("id");
        System.out.println("Board ID: " + listId);

        // Dönen verileri al
        String responseBody1 = response1.prettyPrint();
        String responseBody2 = response2.prettyPrint();
        System.out.println("Response Body - Card1: " + responseBody1);
        System.out.println("Response Body - Card2: " + responseBody2);
    }

    @Test(dependsOnMethods = "createLists")
    public void createCards() {
        newCardName1 = "new_cardFirst";
        newCardName2 = "new_cardSecond";

        JSONObject expectedBody1 = new JSONObject();
        expectedBody1.put("name", newCardName1);

        JSONObject expectedBody2 = new JSONObject();
        expectedBody2.put("name", newCardName2);

        Response response1 = RestAssured.given().baseUri("https://api.trello.com/1/cards").
                queryParam("name",newCardName1).
                queryParam("idList",listId).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                post();

        Response response2 = RestAssured.given().baseUri("https://api.trello.com/1/cards").
                queryParam("name",newCardName2).
                queryParam("idList",listId).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                post();

        response1.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody1 = response1.jsonPath();
        Assert.assertEquals(expectedBody1.get("name"), actualBody1.get("name"));

        response2.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody2 = response2.jsonPath();
        Assert.assertEquals(expectedBody2.get("name"), actualBody2.get("name"));

        firstCardId = response1.jsonPath().getString("id");
        System.out.println("First Card ID: " + firstCardId);

        secondCardId = response2.jsonPath().getString("id");
        System.out.println("Second Card ID" + secondCardId);

        // Dönen verileri al
        String responseBody1 = response1.prettyPrint();
        String responseBody2 = response2.prettyPrint();
        System.out.println("Response Body - Card1: " + responseBody1);
        System.out.println("Response Body - Card2: " + responseBody2);
    }

    @Test(dependsOnMethods = "createCards")
    public void getList() {

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("name", newBoardName);

        Response response = RestAssured.given().baseUri("https://api.trello.com/1/boards/" + boardId + "/").
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                get();

        response.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody = response.jsonPath();
        Assert.assertEquals(expectedBody.get("name"), actualBody.get("name"));

        // Dönen verileri al
        String responseBody = response.prettyPrint();
        System.out.println("Response Body: " + responseBody);
    }

    @Test(dependsOnMethods = "getList")
    public void updateCard() {
        updatedListName = "Yeni_Card_Adı";
        JSONObject expectedBody = new JSONObject();
        expectedBody.put("name", updatedListName);

        // Liste güncelleme isteği gönder
        Response response = RestAssured.given().baseUri("https://api.trello.com/1/cards/" + firstCardId)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .header("Content-Type", "application/json")
                .body("{\"name\":\"" + updatedListName + "\"}")
                .put();

        response.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        JsonPath actualBody = response.jsonPath();
        Assert.assertEquals(expectedBody.get("name"), actualBody.get("name"));

        // Dönen verileri al
        String responseBody = response.prettyPrint();
        System.out.println("Response Body: " + responseBody);
    }

    @Test(dependsOnMethods = "updateCard")
    public void deleteCards() {

        Response response1 = RestAssured.given().baseUri("https://api.trello.com/1/cards/" + firstCardId)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .header("Content-Type", "application/json")
                .delete();

        Response response2 = RestAssured.given().baseUri("https://api.trello.com/1/cards/" + secondCardId)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .header("Content-Type", "application/json")
                .delete();

        response1.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        response2.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);


        String responseBody1 = response1.prettyPrint();
        String responseBody2 = response2.prettyPrint();
        System.out.println("Response Body - Card1: " + responseBody1);
        System.out.println("Response Body - Card2: " + responseBody2);
    }

    @Test(dependsOnMethods = "deleteCards")
    public void deleteBoards() {
        Response response = RestAssured.given().baseUri("https://api.trello.com/1/boards/" + boardId).
                queryParam("key",API_KEY).
                queryParam("token",TOKEN).
                header("Content-Type","application/json").
                delete();

        response.then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(200);

        // Dönen verileri al
        String responseBody = response.prettyPrint();
        System.out.println("Response Body: " + responseBody);
    }
}
