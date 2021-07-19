/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.leijendary.spring.microservicetemplate.event.schema;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class SampleSchema extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -3977931787944776577L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SampleSchema\",\"namespace\":\"com.leijendary.spring.microservicetemplate.event.schema\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"column1\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"column2\",\"type\":\"int\"},{\"name\":\"createdDate\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"createdBy\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"lastModifiedDate\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"lastModifiedBy\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<SampleSchema> ENCODER =
      new BinaryMessageEncoder<SampleSchema>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<SampleSchema> DECODER =
      new BinaryMessageDecoder<SampleSchema>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<SampleSchema> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<SampleSchema> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<SampleSchema> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<SampleSchema>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this SampleSchema to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a SampleSchema from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a SampleSchema instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static SampleSchema fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private long id;
   private java.lang.String column1;
   private int column2;
   private java.lang.String createdDate;
   private java.lang.String createdBy;
   private java.lang.String lastModifiedDate;
   private java.lang.String lastModifiedBy;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public SampleSchema() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param column1 The new value for column1
   * @param column2 The new value for column2
   * @param createdDate The new value for createdDate
   * @param createdBy The new value for createdBy
   * @param lastModifiedDate The new value for lastModifiedDate
   * @param lastModifiedBy The new value for lastModifiedBy
   */
  public SampleSchema(java.lang.Long id, java.lang.String column1, java.lang.Integer column2, java.lang.String createdDate, java.lang.String createdBy, java.lang.String lastModifiedDate, java.lang.String lastModifiedBy) {
    this.id = id;
    this.column1 = column1;
    this.column2 = column2;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastModifiedDate = lastModifiedDate;
    this.lastModifiedBy = lastModifiedBy;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return column1;
    case 2: return column2;
    case 3: return createdDate;
    case 4: return createdBy;
    case 5: return lastModifiedDate;
    case 6: return lastModifiedBy;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: column1 = value$ != null ? value$.toString() : null; break;
    case 2: column2 = (java.lang.Integer)value$; break;
    case 3: createdDate = value$ != null ? value$.toString() : null; break;
    case 4: createdBy = value$ != null ? value$.toString() : null; break;
    case 5: lastModifiedDate = value$ != null ? value$.toString() : null; break;
    case 6: lastModifiedBy = value$ != null ? value$.toString() : null; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'column1' field.
   * @return The value of the 'column1' field.
   */
  public java.lang.String getColumn1() {
    return column1;
  }


  /**
   * Sets the value of the 'column1' field.
   * @param value the value to set.
   */
  public void setColumn1(java.lang.String value) {
    this.column1 = value;
  }

  /**
   * Gets the value of the 'column2' field.
   * @return The value of the 'column2' field.
   */
  public int getColumn2() {
    return column2;
  }


  /**
   * Sets the value of the 'column2' field.
   * @param value the value to set.
   */
  public void setColumn2(int value) {
    this.column2 = value;
  }

  /**
   * Gets the value of the 'createdDate' field.
   * @return The value of the 'createdDate' field.
   */
  public java.lang.String getCreatedDate() {
    return createdDate;
  }


  /**
   * Sets the value of the 'createdDate' field.
   * @param value the value to set.
   */
  public void setCreatedDate(java.lang.String value) {
    this.createdDate = value;
  }

  /**
   * Gets the value of the 'createdBy' field.
   * @return The value of the 'createdBy' field.
   */
  public java.lang.String getCreatedBy() {
    return createdBy;
  }


  /**
   * Sets the value of the 'createdBy' field.
   * @param value the value to set.
   */
  public void setCreatedBy(java.lang.String value) {
    this.createdBy = value;
  }

  /**
   * Gets the value of the 'lastModifiedDate' field.
   * @return The value of the 'lastModifiedDate' field.
   */
  public java.lang.String getLastModifiedDate() {
    return lastModifiedDate;
  }


  /**
   * Sets the value of the 'lastModifiedDate' field.
   * @param value the value to set.
   */
  public void setLastModifiedDate(java.lang.String value) {
    this.lastModifiedDate = value;
  }

  /**
   * Gets the value of the 'lastModifiedBy' field.
   * @return The value of the 'lastModifiedBy' field.
   */
  public java.lang.String getLastModifiedBy() {
    return lastModifiedBy;
  }


  /**
   * Sets the value of the 'lastModifiedBy' field.
   * @param value the value to set.
   */
  public void setLastModifiedBy(java.lang.String value) {
    this.lastModifiedBy = value;
  }

  /**
   * Creates a new SampleSchema RecordBuilder.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder newBuilder() {
    return new com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder();
  }

  /**
   * Creates a new SampleSchema RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder newBuilder(com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder other) {
    if (other == null) {
      return new com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder();
    } else {
      return new com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder(other);
    }
  }

  /**
   * Creates a new SampleSchema RecordBuilder by copying an existing SampleSchema instance.
   * @param other The existing instance to copy.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder newBuilder(com.leijendary.spring.microservicetemplate.event.schema.SampleSchema other) {
    if (other == null) {
      return new com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder();
    } else {
      return new com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder(other);
    }
  }

  /**
   * RecordBuilder for SampleSchema instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SampleSchema>
    implements org.apache.avro.data.RecordBuilder<SampleSchema> {

    private long id;
    private java.lang.String column1;
    private int column2;
    private java.lang.String createdDate;
    private java.lang.String createdBy;
    private java.lang.String lastModifiedDate;
    private java.lang.String lastModifiedBy;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.column1)) {
        this.column1 = data().deepCopy(fields()[1].schema(), other.column1);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.column2)) {
        this.column2 = data().deepCopy(fields()[2].schema(), other.column2);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.createdDate)) {
        this.createdDate = data().deepCopy(fields()[3].schema(), other.createdDate);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.createdBy)) {
        this.createdBy = data().deepCopy(fields()[4].schema(), other.createdBy);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.lastModifiedDate)) {
        this.lastModifiedDate = data().deepCopy(fields()[5].schema(), other.lastModifiedDate);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
      if (isValidValue(fields()[6], other.lastModifiedBy)) {
        this.lastModifiedBy = data().deepCopy(fields()[6].schema(), other.lastModifiedBy);
        fieldSetFlags()[6] = other.fieldSetFlags()[6];
      }
    }

    /**
     * Creates a Builder by copying an existing SampleSchema instance
     * @param other The existing instance to copy.
     */
    private Builder(com.leijendary.spring.microservicetemplate.event.schema.SampleSchema other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.column1)) {
        this.column1 = data().deepCopy(fields()[1].schema(), other.column1);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.column2)) {
        this.column2 = data().deepCopy(fields()[2].schema(), other.column2);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.createdDate)) {
        this.createdDate = data().deepCopy(fields()[3].schema(), other.createdDate);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.createdBy)) {
        this.createdBy = data().deepCopy(fields()[4].schema(), other.createdBy);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.lastModifiedDate)) {
        this.lastModifiedDate = data().deepCopy(fields()[5].schema(), other.lastModifiedDate);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.lastModifiedBy)) {
        this.lastModifiedBy = data().deepCopy(fields()[6].schema(), other.lastModifiedBy);
        fieldSetFlags()[6] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'column1' field.
      * @return The value.
      */
    public java.lang.String getColumn1() {
      return column1;
    }


    /**
      * Sets the value of the 'column1' field.
      * @param value The value of 'column1'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setColumn1(java.lang.String value) {
      validate(fields()[1], value);
      this.column1 = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'column1' field has been set.
      * @return True if the 'column1' field has been set, false otherwise.
      */
    public boolean hasColumn1() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'column1' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearColumn1() {
      column1 = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'column2' field.
      * @return The value.
      */
    public int getColumn2() {
      return column2;
    }


    /**
      * Sets the value of the 'column2' field.
      * @param value The value of 'column2'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setColumn2(int value) {
      validate(fields()[2], value);
      this.column2 = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'column2' field has been set.
      * @return True if the 'column2' field has been set, false otherwise.
      */
    public boolean hasColumn2() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'column2' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearColumn2() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'createdDate' field.
      * @return The value.
      */
    public java.lang.String getCreatedDate() {
      return createdDate;
    }


    /**
      * Sets the value of the 'createdDate' field.
      * @param value The value of 'createdDate'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setCreatedDate(java.lang.String value) {
      validate(fields()[3], value);
      this.createdDate = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'createdDate' field has been set.
      * @return True if the 'createdDate' field has been set, false otherwise.
      */
    public boolean hasCreatedDate() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'createdDate' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearCreatedDate() {
      createdDate = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'createdBy' field.
      * @return The value.
      */
    public java.lang.String getCreatedBy() {
      return createdBy;
    }


    /**
      * Sets the value of the 'createdBy' field.
      * @param value The value of 'createdBy'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setCreatedBy(java.lang.String value) {
      validate(fields()[4], value);
      this.createdBy = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'createdBy' field has been set.
      * @return True if the 'createdBy' field has been set, false otherwise.
      */
    public boolean hasCreatedBy() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'createdBy' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearCreatedBy() {
      createdBy = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'lastModifiedDate' field.
      * @return The value.
      */
    public java.lang.String getLastModifiedDate() {
      return lastModifiedDate;
    }


    /**
      * Sets the value of the 'lastModifiedDate' field.
      * @param value The value of 'lastModifiedDate'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setLastModifiedDate(java.lang.String value) {
      validate(fields()[5], value);
      this.lastModifiedDate = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'lastModifiedDate' field has been set.
      * @return True if the 'lastModifiedDate' field has been set, false otherwise.
      */
    public boolean hasLastModifiedDate() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'lastModifiedDate' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearLastModifiedDate() {
      lastModifiedDate = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'lastModifiedBy' field.
      * @return The value.
      */
    public java.lang.String getLastModifiedBy() {
      return lastModifiedBy;
    }


    /**
      * Sets the value of the 'lastModifiedBy' field.
      * @param value The value of 'lastModifiedBy'.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder setLastModifiedBy(java.lang.String value) {
      validate(fields()[6], value);
      this.lastModifiedBy = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'lastModifiedBy' field has been set.
      * @return True if the 'lastModifiedBy' field has been set, false otherwise.
      */
    public boolean hasLastModifiedBy() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'lastModifiedBy' field.
      * @return This builder.
      */
    public com.leijendary.spring.microservicetemplate.event.schema.SampleSchema.Builder clearLastModifiedBy() {
      lastModifiedBy = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SampleSchema build() {
      try {
        SampleSchema record = new SampleSchema();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.column1 = fieldSetFlags()[1] ? this.column1 : (java.lang.String) defaultValue(fields()[1]);
        record.column2 = fieldSetFlags()[2] ? this.column2 : (java.lang.Integer) defaultValue(fields()[2]);
        record.createdDate = fieldSetFlags()[3] ? this.createdDate : (java.lang.String) defaultValue(fields()[3]);
        record.createdBy = fieldSetFlags()[4] ? this.createdBy : (java.lang.String) defaultValue(fields()[4]);
        record.lastModifiedDate = fieldSetFlags()[5] ? this.lastModifiedDate : (java.lang.String) defaultValue(fields()[5]);
        record.lastModifiedBy = fieldSetFlags()[6] ? this.lastModifiedBy : (java.lang.String) defaultValue(fields()[6]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<SampleSchema>
    WRITER$ = (org.apache.avro.io.DatumWriter<SampleSchema>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<SampleSchema>
    READER$ = (org.apache.avro.io.DatumReader<SampleSchema>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.id);

    out.writeString(this.column1);

    out.writeInt(this.column2);

    out.writeString(this.createdDate);

    out.writeString(this.createdBy);

    out.writeString(this.lastModifiedDate);

    out.writeString(this.lastModifiedBy);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.column1 = in.readString();

      this.column2 = in.readInt();

      this.createdDate = in.readString();

      this.createdBy = in.readString();

      this.lastModifiedDate = in.readString();

      this.lastModifiedBy = in.readString();

    } else {
      for (int i = 0; i < 7; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.column1 = in.readString();
          break;

        case 2:
          this.column2 = in.readInt();
          break;

        case 3:
          this.createdDate = in.readString();
          break;

        case 4:
          this.createdBy = in.readString();
          break;

        case 5:
          this.lastModifiedDate = in.readString();
          break;

        case 6:
          this.lastModifiedBy = in.readString();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










