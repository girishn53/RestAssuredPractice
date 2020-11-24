package qaGirish.RestAssuredPractice;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DyanmicJson {

	@Test(dataProvider = "BooksData")

	public void addBook(String isbn, String isle) {

		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(Payload.addBook(isbn, isle)).when()
				.post("/Library/Addbook.php").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		JsonPath js = new JsonPath(response);

		String Id = js.get("ID");

		System.out.println("ID is " + Id);

	}

	@DataProvider(name = "BooksData")

	public Object[][] getData() {

		return new Object[][] { { "abcdefgh", "1234233" }, { "efghigjk", "567823325" }, { "ijkldre", "91014333" }

		};

	}
}
