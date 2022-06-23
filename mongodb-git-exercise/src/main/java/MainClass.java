import java.time.LocalDate;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;

public class MainClass {

	static Scanner s = new Scanner(System.in);
	
	static MongoClient mongoClient = MongoClients.create();
	static MongoDatabase mongoDatabase = mongoClient.getDatabase("mongo-exercise");
	static MongoCollection<Document> productsCollection = mongoDatabase.getCollection("products");

	public static void main(String[] args) {
		boolean run = true;
		
		while(run) {
			displayMenu();
			
			System.out.println("Would you like to continue using the system? ");
			run = s.nextBoolean();
		}
		
	}
	
	public static void displayMenu() {
		System.out.println("1. Add products");
		System.out.println("2. Query database");
		
		int option = s.nextInt();
		switch(option) {
			case 1:
				addProduct();
				break;
			case 2:
				queryDatabase();
				break;
		}
	}
	
	public static void addProduct() {
		Document doc = new Document("_id", new ObjectId());
		doc.append("name", "Drums");
		doc.append("price", 700);
		doc.append("date_added", LocalDate.now());
		doc.append("stock", 8);
		doc.append("product_type", "Music");
		
		try {
			InsertOneResult result = productsCollection.insertOne(doc);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println("Product not added due to an error.");
		}
	}
	
	public static void queryDatabase() {
		System.out.println("Choose one of the following options: ");
		System.out.println("1. Find by type");
		System.out.println("2. Find by name contains");
		System.out.println("3. Find all alphabetically based on name");
		System.out.println("4. Find all ordered by price");
		System.out.println("5. Find all with price between range");
		
		int option = s.nextInt();
		switch(option) {
			case 1:
				findByType();
				break;
			case 2:
				findByNameContains();
				break;
			case 3:
				findOrderedByName();
				break;
			case 4:
				findOrderedByPrice();
				break;
			case 5:
				findWithPriceBetween();
				break;
			default:
				findAll();
		}
	}
	
	private static void findByType() {
		System.out.println("Type: ");
		String type = s.next();
		
		Bson queryFilter = Filters.eq("product_type", type);
		MongoCursor<Document> cursor = productsCollection.find(queryFilter).iterator();

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}
	
	private static void findByNameContains() {
		System.out.println("Name (contains): ");
		String name = s.next();
		
		Bson queryFilter = Filters.regex("name", name);
		MongoCursor<Document> cursor = productsCollection.find(queryFilter).iterator();

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}
	
	private static void findOrderedByName() {
		MongoCursor<Document> cursor = productsCollection.find().sort(Sorts.ascending("name")).iterator();

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}
	
	private static void findOrderedByPrice() {
		MongoCursor<Document> cursor = productsCollection.find().sort(Sorts.ascending("price")).iterator();

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}
	
	private static void findWithPriceBetween() {
		System.out.println("Inform two prices (lower bound first) to be used (between): ");
		int lowerBound= s.nextInt();
		int upperBound = s.nextInt();
		
		Bson queryFilter = Filters.and(Filters.gt("price", lowerBound), Filters.lt("price", upperBound));
		MongoCursor<Document> cursor = productsCollection.find(queryFilter).iterator();

		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}

	public static void findAll() {
		Bson queryFilter = Filters.empty();
		MongoCursor<Document> cursor = productsCollection.find(queryFilter).iterator();
		while(cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
	}

}
