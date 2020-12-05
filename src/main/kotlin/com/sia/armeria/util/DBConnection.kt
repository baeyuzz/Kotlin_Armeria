package com.sia.armeria.util

import com.google.gson.Gson
import com.mongodb.BasicDBObject
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.connection.ClusterSettings
import com.sia.armeria.model.ImageProfile
import org.bson.Document
import org.bson.types.ObjectId
import com.mongodb.client.*

import java.util.*


class DBConnection {

    private var mongoClient: MongoClient
    private var database: MongoDatabase
    private var collection: MongoCollection<Document>


    init {
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings { builder: ClusterSettings.Builder -> builder.hosts(Arrays.asList(ServerAddress("localhost", 27017))) }
                        .build())
        database = mongoClient.getDatabase("sia")
        collection = database.getCollection("ImageProfile")
    }

    fun insert(imageProfile: ImageProfile) {
        val doc = Document.parse(Gson().toJson(imageProfile))
        collection.insertOne(doc)
    }

    fun findAll(): FindIterable<Document> {
        return collection.find() as FindIterable<Document>
    }

    fun findOne(id: String): FindIterable<Document> {
        val query = BasicDBObject()
        query["_id"] = ObjectId(id)

        return collection.find(query)
    }

//    fun close() {
//        try {
//            mongoClient.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}