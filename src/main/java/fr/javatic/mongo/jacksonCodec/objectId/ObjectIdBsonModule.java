package fr.javatic.mongo.jacksonCodec.objectId;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import org.bson.types.ObjectId;

public class ObjectIdBsonModule extends Module {
  @Override
  public String getModuleName() {
    return "ObjectIdBsonModule";
  }

  @Override
  public Version version() {
    return new Version(0,1,0,"","","");
  }

  @Override
  public void setupModule(SetupContext context) {
    SimpleSerializers serializers = new SimpleSerializers();
    serializers.addSerializer(ObjectId.class, new BsonObjectIdSerializer());
    context.addSerializers(serializers);

    SimpleDeserializers deserializers = new SimpleDeserializers();
    deserializers.addDeserializer(ObjectId.class, new BsonObjectIdDeserializer());
    context.addDeserializers(deserializers);

  }
}
