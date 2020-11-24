package qaGirish.RestAssuredPractice;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;
import pojo.Api;
import pojo.GetCourse;
import pojo.webAutomation;

public class oAuthTest {

	// getcode
	// https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php

	public static void main(String[] args) throws InterruptedException {

		String[] courseTitles = { "Selenium Webdriver Java", "Cypress", "Protractor" };

		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F5wHrb7UZfxBuIggcWEsMrLeWta5klznZsut8aUdboRXJ71sxocRjPwr16w9lw08rwhVWokOLMHRogp2IYoLYfEI&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";

		String partialcode = url.split("code=")[1];

		String code = partialcode.split("&scope")[0];

		System.out.println(code);

		String response =

				given()

						.urlEncodingEnabled(false)

						.queryParams("code", code)

						.queryParams("client_id",
								"692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")

						.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")

						.queryParams("grant_type", "authorization_code")

						.queryParams("state", "verifyfjdss")

						.queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")

						.queryParam("scope", "email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")

						.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")

						.when().log().all()

						.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		System.out.println("response is " + response);

		JsonPath jsonPath = new JsonPath(response);

		String accessToken = jsonPath.getString("access_token");

		System.out.println("accessToken is " + accessToken);

		JsonPath jsonPath1 = new JsonPath(response);

		String accessToken1 = jsonPath.getString("access_token");

		System.out.println(accessToken1);

		GetCourse gc = given().contentType("application/json").

				queryParams("access_token", accessToken)
				.expect().defaultParser(Parser.JSON)

				.when()

				.get("https://rahulshettyacademy.com/getCourse.php")

				.as(GetCourse.class);

		System.out.println(gc.getInstructor());

		List<Api> apiCourses = gc.getCourses().getApi();

		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
				System.out.println(apiCourses.get(i).getPrice());

				break;
			}

		}

		// get Courses

		ArrayList<String> al = new ArrayList<String>();

		List<webAutomation> webcourses = gc.getCourses().getWebAutomation();

		for (int i = 0; i < webcourses.size(); i++) {

			al.add(webcourses.get(i).getCourseTitle());
		}

		List<String> expectedList = Arrays.asList(courseTitles);

		Assert.assertTrue(al.equals(expectedList));

	}

}
