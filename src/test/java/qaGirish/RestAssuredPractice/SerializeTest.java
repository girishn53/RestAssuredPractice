package qaGirish.RestAssuredPractice;

import io.restassured.RestAssured;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;
public class SerializeTest {
	
	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		AddPlace p=new AddPlace();
		
		p.setAccuracy(50);
		
		p.setAddress("Plot no 14");
		
		p.setLanguage("English");
		
		p.setName("Home1");
		
		p.setPhone_number("9413835856");
		
		p.setWebsite("Https://www.gk.com");
		Location l=new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		
		
		p.setLocation(l);
		
		List<String> myList=new ArrayList<String>();
		myList.add("shoe park");
		
		myList.add("shop");
		
		
		p.setTypes(myList);
		
		String resp = given()
		.queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body(p)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(resp);
		
		
		
		
		
	}

}
