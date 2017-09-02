package fr.javatic.mongo.jacksonCodec;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.javatic.mongo.jacksonCodec.javaTime.JavaTimeBsonModule;
import fr.javatic.mongo.jacksonCodec.objectId.ObjectIdBsonModule;
import org.bson.*;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Objects;

import static junit.framework.TestCase.assertEquals;

/**
 * Test whether the BsonObjectIdSerializer produces the same bson
 * as the MongoDB ObjectId serializer.
 */
public class BsonObjectIdTest {

  private final JacksonCodec<ObjectIdWrapper> codec = new JacksonCodec<>(
      ObjectMapperFactory.createObjectMapper().registerModule(new ObjectIdBsonModule()),
      CodecRegistries.fromProviders(new BsonValueCodecProvider()),
      ObjectIdWrapper.class);

  public static class ObjectIdWrapper {
    @JsonProperty("objectId")
    public ObjectId objectId;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ObjectIdWrapper that = (ObjectIdWrapper) o;
      return Objects.equals(objectId, that.objectId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(objectId);
    }
  }

  private static BsonDocument createObjectIdWrapperBson(ObjectId id) {
    BsonDocument expectedDocument = new BsonDocument();
    expectedDocument.put("objectId", id == null ? new BsonNull() : new BsonObjectId(id));
    return expectedDocument.toBsonDocument(null, null);
  }

  @Test
  public void testEncoding() {
    BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());

    ObjectIdWrapper wrapper = new ObjectIdWrapper();
    wrapper.objectId = new ObjectId();
    codec.encode(writer,wrapper, EncoderContext.builder().build());
    assertEquals(createObjectIdWrapperBson(wrapper.objectId), writer.getDocument());

    // when null:
    writer = new BsonDocumentWriter(new BsonDocument());
    wrapper.objectId = null;
    codec.encode(writer,wrapper, EncoderContext.builder().build());
    assertEquals(createObjectIdWrapperBson(wrapper.objectId), writer.getDocument());
  }

  @Test
  public void testDecoding() {
    ObjectId id = new ObjectId();
    BsonDocumentReader reader = new BsonDocumentReader(createObjectIdWrapperBson(id));

    ObjectIdWrapper wrapper = codec.decode(reader, DecoderContext.builder().build());

    assertEquals(id.getTimestamp(), wrapper.objectId.getTimestamp());
    assertEquals(id.getMachineIdentifier(), wrapper.objectId.getMachineIdentifier());
    assertEquals(id.getProcessIdentifier(), wrapper.objectId.getProcessIdentifier());
    assertEquals(id.getCounter(), wrapper.objectId.getCounter());

    // when null:
    reader = new BsonDocumentReader(createObjectIdWrapperBson(null));
    wrapper = codec.decode(reader, DecoderContext.builder().build());
    assertEquals(null, wrapper.objectId);
  }



}
