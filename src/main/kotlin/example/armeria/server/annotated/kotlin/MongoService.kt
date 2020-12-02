package example.armeria.server.annotated.kotlin

import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import example.armeria.server.annotated.model.Profile
import org.bson.Document
import org.bson.types.ObjectId


class MongoService {

    private val MONGO_PORT = 27017
    private val MONGO_HOST = "localhost"
    private val DB_NAME = "sia"

    private var mongo: MongoClient? = null
    private var col: MongoCollection<Document>? = null

    fun MongoService() {
        mongo = MongoClient(ServerAddress(MONGO_HOST, MONGO_PORT))
        val db: MongoDatabase = mongo!!.getDatabase(DB_NAME)
        col = db.getCollection("imgProfile")
    }


    fun close() {
        try {
            mongo?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insert(profile: Profile) {
        val doc = Document.parse(Gson().toJson(profile))
        col?.insertOne(doc)
    }


    fun findAll(): FindIterable<Document> {

        val res: FindIterable<Document> = col?.find() as FindIterable<Document>

        return res
    }


    fun findOne(id: String): FindIterable<Document>? {
        val query = BasicDBObject()
        query["_id"] = ObjectId(id)

        return col?.find(query)
    }
}