package qaGirish.RestAssuredPractice;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class JiraTest {

	public static void main(String[] args) {

		RestAssured.baseURI = "http://localhost:8080";

		SessionFilter session = new SessionFilter();

		String resp = given().header("Content-Type", "application/json").log().all()
				.body("{ \"username\": \"girishn53\", \r\n" + "\"password\": \"omegaf99*\" \r\n" + "}")
				.filter(session)
				.when()
				.post("/rest/auth/1/session")
				.then().log().all().extract().response().asString();

		// Add comment
		String addCommentResponse = given().pathParam("id", "10006")
				.header("Content-Type", "application/json").log()
				.all()
				.body("{\r\n" + "\"body\": \"This is a my fist comment from rest api\",\r\n" + "\"visibility\": {\r\n"
						+ "\"type\": \"role\",\r\n" + "\"value\": \"Administrators\"\r\n" + "}\r\n" + "}")
				.filter(session)
				.when()
				.post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat()
				.statusCode(201).extract().asString();

		JsonPath js = new JsonPath(addCommentResponse);

		int commentId = js.getInt("id");

		// Add attachment

		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10006")
				.header("Content-Type", "multipart/form-data")

				.multiPart("file", new File("jira.txt"))
				.when().post("/rest/api/2/issue/{id}/attachments").then()
				.assertThat().statusCode(200);

		// getIssue

		// String issueDetails
		// =given().filter(session).pathParam("id","10006").log().all()
		// .when().get("/rest/api/2/issue/{id}")
		// .then().log().all().extract().response().toString();
		//
		// System.out.println("issue Details are "+issueDetails);

		String issueDetails = given().filter(session).pathParam("key", "10006")

				.queryParam("fields", "comment")

				.log().all().when().get("/rest/api/2/issue/{key}").then()

				.log().all().extract().response().asString();

		System.out.println("issue Details are " + issueDetails);

		JsonPath path = new JsonPath(issueDetails);

		int count = path.getInt("fields.comment.comments.size()");

		System.out.println("count is " + count);

		for (int i = 0; i < count; i++) {
			String message;
			String comId = path.get("fields.comment.comments[" + i + "].id").toString();

			if (comId.equalsIgnoreCase("10006"))

			{
				message = path.get("fields.comment.comments[" + i + "].body").toString();
				System.out.println("message " + message);
			}

		}

	}

}
