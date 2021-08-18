/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.leijendary.schema;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class SampleSchema extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 9193670717509829484L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SampleSchema\",\"namespace\":\"com.leijendary.schema\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"column1\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"column2\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"translations\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"SampleTranslationSchema\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"description\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"language\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"ordinal\",\"type\":\"int\"}]}}}]}");
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
   private java.lang.String column2;
   private java.util.List<com.leijendary.schema.SampleTranslationSchema> translations;

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
   * @param translations The new value for translations
   */
  public SampleSchema(java.lang.Long id, java.lang.String column1, java.lang.String column2, java.util.List<com.leijendary.schema.SampleTranslationSchema> translations) {
    this.id = id;
    this.column1 = column1;
    this.column2 = column2;
    this.translations = translations;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return column1;
    case 2: return column2;
    case 3: return translations;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: column1 = value$ != null ? value$.toString() : null; break;
    case 2: column2 = value$ != null ? value$.toString() : null; break;
    case 3: translations = (java.util.List<com.leijendary.schema.SampleTranslationSchema>)value$; break;
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
  public java.lang.String getColumn2() {
    return column2;
  }


  /**
   * Sets the value of the 'column2' field.
   * @param value the value to set.
   */
  public void setColumn2(java.lang.String value) {
    this.column2 = value;
  }

  /**
   * Gets the value of the 'translations' field.
   * @return The value of the 'translations' field.
   */
  public java.util.List<com.leijendary.schema.SampleTranslationSchema> getTranslations() {
    return translations;
  }


  /**
   * Sets the value of the 'translations' field.
   * @param value the value to set.
   */
  public void setTranslations(java.util.List<com.leijendary.schema.SampleTranslationSchema> value) {
    this.translations = value;
  }

  /**
   * Creates a new SampleSchema RecordBuilder.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.schema.SampleSchema.Builder newBuilder() {
    return new com.leijendary.schema.SampleSchema.Builder();
  }

  /**
   * Creates a new SampleSchema RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.schema.SampleSchema.Builder newBuilder(com.leijendary.schema.SampleSchema.Builder other) {
    if (other == null) {
      return new com.leijendary.schema.SampleSchema.Builder();
    } else {
      return new com.leijendary.schema.SampleSchema.Builder(other);
    }
  }

  /**
   * Creates a new SampleSchema RecordBuilder by copying an existing SampleSchema instance.
   * @param other The existing instance to copy.
   * @return A new SampleSchema RecordBuilder
   */
  public static com.leijendary.schema.SampleSchema.Builder newBuilder(com.leijendary.schema.SampleSchema other) {
    if (other == null) {
      return new com.leijendary.schema.SampleSchema.Builder();
    } else {
      return new com.leijendary.schema.SampleSchema.Builder(other);
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
    private java.lang.String column2;
    private java.util.List<com.leijendary.schema.SampleTranslationSchema> translations;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.leijendary.schema.SampleSchema.Builder other) {
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
      if (isValidValue(fields()[3], other.translations)) {
        this.translations = data().deepCopy(fields()[3].schema(), other.translations);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing SampleSchema instance
     * @param other The existing instance to copy.
     */
    private Builder(com.leijendary.schema.SampleSchema other) {
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
      if (isValidValue(fields()[3], other.translations)) {
        this.translations = data().deepCopy(fields()[3].schema(), other.translations);
        fieldSetFlags()[3] = true;
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
    public com.leijendary.schema.SampleSchema.Builder setId(long value) {
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
    public com.leijendary.schema.SampleSchema.Builder clearId() {
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
    public com.leijendary.schema.SampleSchema.Builder setColumn1(java.lang.String value) {
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
    public com.leijendary.schema.SampleSchema.Builder clearColumn1() {
      column1 = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'column2' field.
      * @return The value.
      */
    public java.lang.String getColumn2() {
      return column2;
    }


    /**
      * Sets the value of the 'column2' field.
      * @param value The value of 'column2'.
      * @return This builder.
      */
    public com.leijendary.schema.SampleSchema.Builder setColumn2(java.lang.String value) {
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
    public com.leijendary.schema.SampleSchema.Builder clearColumn2() {
      column2 = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'translations' field.
      * @return The value.
      */
    public java.util.List<com.leijendary.schema.SampleTranslationSchema> getTranslations() {
      return translations;
    }


    /**
      * Sets the value of the 'translations' field.
      * @param value The value of 'translations'.
      * @return This builder.
      */
    public com.leijendary.schema.SampleSchema.Builder setTranslations(java.util.List<com.leijendary.schema.SampleTranslationSchema> value) {
      validate(fields()[3], value);
      this.translations = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'translations' field has been set.
      * @return True if the 'translations' field has been set, false otherwise.
      */
    public boolean hasTranslations() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'translations' field.
      * @return This builder.
      */
    public com.leijendary.schema.SampleSchema.Builder clearTranslations() {
      translations = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SampleSchema build() {
      try {
        SampleSchema record = new SampleSchema();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.column1 = fieldSetFlags()[1] ? this.column1 : (java.lang.String) defaultValue(fields()[1]);
        record.column2 = fieldSetFlags()[2] ? this.column2 : (java.lang.String) defaultValue(fields()[2]);
        record.translations = fieldSetFlags()[3] ? this.translations : (java.util.List<com.leijendary.schema.SampleTranslationSchema>) defaultValue(fields()[3]);
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

    out.writeString(this.column2);

    long size0 = this.translations.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (com.leijendary.schema.SampleTranslationSchema e0: this.translations) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.column1 = in.readString();

      this.column2 = in.readString();

      long size0 = in.readArrayStart();
      java.util.List<com.leijendary.schema.SampleTranslationSchema> a0 = this.translations;
      if (a0 == null) {
        a0 = new SpecificData.Array<com.leijendary.schema.SampleTranslationSchema>((int)size0, SCHEMA$.getField("translations").schema());
        this.translations = a0;
      } else a0.clear();
      SpecificData.Array<com.leijendary.schema.SampleTranslationSchema> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.leijendary.schema.SampleTranslationSchema>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          com.leijendary.schema.SampleTranslationSchema e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new com.leijendary.schema.SampleTranslationSchema();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.column1 = in.readString();
          break;

        case 2:
          this.column2 = in.readString();
          break;

        case 3:
          long size0 = in.readArrayStart();
          java.util.List<com.leijendary.schema.SampleTranslationSchema> a0 = this.translations;
          if (a0 == null) {
            a0 = new SpecificData.Array<com.leijendary.schema.SampleTranslationSchema>((int)size0, SCHEMA$.getField("translations").schema());
            this.translations = a0;
          } else a0.clear();
          SpecificData.Array<com.leijendary.schema.SampleTranslationSchema> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.leijendary.schema.SampleTranslationSchema>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              com.leijendary.schema.SampleTranslationSchema e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new com.leijendary.schema.SampleTranslationSchema();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










