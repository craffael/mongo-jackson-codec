package fr.javatic.mongo.jacksonCodec.objectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.undercouch.bson4jackson.BsonGenerator;
import de.undercouch.bson4jackson.serializers.BsonSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class BsonObjectIdSerializer extends BsonSerializer<ObjectId> {

  @Override
  public void serialize(ObjectId value, BsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
    if(value == null) {
      serializerProvider.defaultSerializeNull(gen);
      return;
    }

    int timestamp = value.getTimestamp();
    int machineIdentifier = (value.getMachineIdentifier() << 8) + (value.getProcessIdentifier()>>8);
    int inc = (value.getProcessIdentifier() << 24) + value.getCounter();
    de.undercouch.bson4jackson.types.ObjectId id = new de.undercouch.bson4jackson.types.ObjectId(timestamp, machineIdentifier, inc);
    ((BsonGenerator) gen).writeObjectId(id);
  }
}
