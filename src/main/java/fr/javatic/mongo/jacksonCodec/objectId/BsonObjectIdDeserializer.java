package fr.javatic.mongo.jacksonCodec.objectId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import de.undercouch.bson4jackson.BsonConstants;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class BsonObjectIdDeserializer extends BsonDeserializer<ObjectId> {

  @Override
  public ObjectId deserialize(BsonParser bsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    if(bsonParser.getCurrentToken() != JsonToken.VALUE_EMBEDDED_OBJECT ||
        bsonParser.getCurrentBsonType() != BsonConstants.TYPE_OBJECTID) {
      throw ctxt.mappingException(ObjectId.class);
    }

    Object obj = bsonParser.getEmbeddedObject();
    if(obj == null) {
      return null;
    }

    de.undercouch.bson4jackson.types.ObjectId id = (de.undercouch.bson4jackson.types.ObjectId) bsonParser.getEmbeddedObject();
    int timestamp = id.getTime();
    int machineIdentifier = (id.getMachine()>>8);
    short processId = (short) (((id.getMachine()<<24) >>16) + (id.getInc() >> 24) + (id.getInc() < 0 ? 256 : 0));
    int counter = (id.getInc()<<8) >> 8;
    if(counter < 0 ) counter += (1 << 24);
    return new ObjectId(timestamp, machineIdentifier, processId, counter);
  }
}
