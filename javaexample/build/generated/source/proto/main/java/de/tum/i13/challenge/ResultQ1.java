// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package de.tum.i13.challenge;

/**
 * Protobuf type {@code Challenger.ResultQ1}
 */
public final class ResultQ1 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Challenger.ResultQ1)
    ResultQ1OrBuilder {
private static final long serialVersionUID = 0L;
  // Use ResultQ1.newBuilder() to construct.
  private ResultQ1(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ResultQ1() {
    indicators_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ResultQ1();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ResultQ1(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            benchmarkId_ = input.readInt64();
            break;
          }
          case 16: {

            batchSeqId_ = input.readInt64();
            break;
          }
          case 26: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              indicators_ = new java.util.ArrayList<de.tum.i13.challenge.Indicator>();
              mutable_bitField0_ |= 0x00000001;
            }
            indicators_.add(
                input.readMessage(de.tum.i13.challenge.Indicator.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        indicators_ = java.util.Collections.unmodifiableList(indicators_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ1_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ1_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            de.tum.i13.challenge.ResultQ1.class, de.tum.i13.challenge.ResultQ1.Builder.class);
  }

  public static final int BENCHMARK_ID_FIELD_NUMBER = 1;
  private long benchmarkId_;
  /**
   * <code>int64 benchmark_id = 1;</code>
   * @return The benchmarkId.
   */
  @java.lang.Override
  public long getBenchmarkId() {
    return benchmarkId_;
  }

  public static final int BATCH_SEQ_ID_FIELD_NUMBER = 2;
  private long batchSeqId_;
  /**
   * <code>int64 batch_seq_id = 2;</code>
   * @return The batchSeqId.
   */
  @java.lang.Override
  public long getBatchSeqId() {
    return batchSeqId_;
  }

  public static final int INDICATORS_FIELD_NUMBER = 3;
  private java.util.List<de.tum.i13.challenge.Indicator> indicators_;
  /**
   * <code>repeated .Challenger.Indicator indicators = 3;</code>
   */
  @java.lang.Override
  public java.util.List<de.tum.i13.challenge.Indicator> getIndicatorsList() {
    return indicators_;
  }
  /**
   * <code>repeated .Challenger.Indicator indicators = 3;</code>
   */
  @java.lang.Override
  public java.util.List<? extends de.tum.i13.challenge.IndicatorOrBuilder> 
      getIndicatorsOrBuilderList() {
    return indicators_;
  }
  /**
   * <code>repeated .Challenger.Indicator indicators = 3;</code>
   */
  @java.lang.Override
  public int getIndicatorsCount() {
    return indicators_.size();
  }
  /**
   * <code>repeated .Challenger.Indicator indicators = 3;</code>
   */
  @java.lang.Override
  public de.tum.i13.challenge.Indicator getIndicators(int index) {
    return indicators_.get(index);
  }
  /**
   * <code>repeated .Challenger.Indicator indicators = 3;</code>
   */
  @java.lang.Override
  public de.tum.i13.challenge.IndicatorOrBuilder getIndicatorsOrBuilder(
      int index) {
    return indicators_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (benchmarkId_ != 0L) {
      output.writeInt64(1, benchmarkId_);
    }
    if (batchSeqId_ != 0L) {
      output.writeInt64(2, batchSeqId_);
    }
    for (int i = 0; i < indicators_.size(); i++) {
      output.writeMessage(3, indicators_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (benchmarkId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, benchmarkId_);
    }
    if (batchSeqId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, batchSeqId_);
    }
    for (int i = 0; i < indicators_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, indicators_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof de.tum.i13.challenge.ResultQ1)) {
      return super.equals(obj);
    }
    de.tum.i13.challenge.ResultQ1 other = (de.tum.i13.challenge.ResultQ1) obj;

    if (getBenchmarkId()
        != other.getBenchmarkId()) return false;
    if (getBatchSeqId()
        != other.getBatchSeqId()) return false;
    if (!getIndicatorsList()
        .equals(other.getIndicatorsList())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + BENCHMARK_ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getBenchmarkId());
    hash = (37 * hash) + BATCH_SEQ_ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getBatchSeqId());
    if (getIndicatorsCount() > 0) {
      hash = (37 * hash) + INDICATORS_FIELD_NUMBER;
      hash = (53 * hash) + getIndicatorsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ1 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ1 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ1 parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(de.tum.i13.challenge.ResultQ1 prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code Challenger.ResultQ1}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Challenger.ResultQ1)
      de.tum.i13.challenge.ResultQ1OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ1_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ1_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              de.tum.i13.challenge.ResultQ1.class, de.tum.i13.challenge.ResultQ1.Builder.class);
    }

    // Construct using de.tum.i13.challenge.ResultQ1.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getIndicatorsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      benchmarkId_ = 0L;

      batchSeqId_ = 0L;

      if (indicatorsBuilder_ == null) {
        indicators_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        indicatorsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ1_descriptor;
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ1 getDefaultInstanceForType() {
      return de.tum.i13.challenge.ResultQ1.getDefaultInstance();
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ1 build() {
      de.tum.i13.challenge.ResultQ1 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ1 buildPartial() {
      de.tum.i13.challenge.ResultQ1 result = new de.tum.i13.challenge.ResultQ1(this);
      int from_bitField0_ = bitField0_;
      result.benchmarkId_ = benchmarkId_;
      result.batchSeqId_ = batchSeqId_;
      if (indicatorsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          indicators_ = java.util.Collections.unmodifiableList(indicators_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.indicators_ = indicators_;
      } else {
        result.indicators_ = indicatorsBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof de.tum.i13.challenge.ResultQ1) {
        return mergeFrom((de.tum.i13.challenge.ResultQ1)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(de.tum.i13.challenge.ResultQ1 other) {
      if (other == de.tum.i13.challenge.ResultQ1.getDefaultInstance()) return this;
      if (other.getBenchmarkId() != 0L) {
        setBenchmarkId(other.getBenchmarkId());
      }
      if (other.getBatchSeqId() != 0L) {
        setBatchSeqId(other.getBatchSeqId());
      }
      if (indicatorsBuilder_ == null) {
        if (!other.indicators_.isEmpty()) {
          if (indicators_.isEmpty()) {
            indicators_ = other.indicators_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureIndicatorsIsMutable();
            indicators_.addAll(other.indicators_);
          }
          onChanged();
        }
      } else {
        if (!other.indicators_.isEmpty()) {
          if (indicatorsBuilder_.isEmpty()) {
            indicatorsBuilder_.dispose();
            indicatorsBuilder_ = null;
            indicators_ = other.indicators_;
            bitField0_ = (bitField0_ & ~0x00000001);
            indicatorsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getIndicatorsFieldBuilder() : null;
          } else {
            indicatorsBuilder_.addAllMessages(other.indicators_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      de.tum.i13.challenge.ResultQ1 parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (de.tum.i13.challenge.ResultQ1) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private long benchmarkId_ ;
    /**
     * <code>int64 benchmark_id = 1;</code>
     * @return The benchmarkId.
     */
    @java.lang.Override
    public long getBenchmarkId() {
      return benchmarkId_;
    }
    /**
     * <code>int64 benchmark_id = 1;</code>
     * @param value The benchmarkId to set.
     * @return This builder for chaining.
     */
    public Builder setBenchmarkId(long value) {
      
      benchmarkId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 benchmark_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearBenchmarkId() {
      
      benchmarkId_ = 0L;
      onChanged();
      return this;
    }

    private long batchSeqId_ ;
    /**
     * <code>int64 batch_seq_id = 2;</code>
     * @return The batchSeqId.
     */
    @java.lang.Override
    public long getBatchSeqId() {
      return batchSeqId_;
    }
    /**
     * <code>int64 batch_seq_id = 2;</code>
     * @param value The batchSeqId to set.
     * @return This builder for chaining.
     */
    public Builder setBatchSeqId(long value) {
      
      batchSeqId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 batch_seq_id = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearBatchSeqId() {
      
      batchSeqId_ = 0L;
      onChanged();
      return this;
    }

    private java.util.List<de.tum.i13.challenge.Indicator> indicators_ =
      java.util.Collections.emptyList();
    private void ensureIndicatorsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        indicators_ = new java.util.ArrayList<de.tum.i13.challenge.Indicator>(indicators_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.tum.i13.challenge.Indicator, de.tum.i13.challenge.Indicator.Builder, de.tum.i13.challenge.IndicatorOrBuilder> indicatorsBuilder_;

    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public java.util.List<de.tum.i13.challenge.Indicator> getIndicatorsList() {
      if (indicatorsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(indicators_);
      } else {
        return indicatorsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public int getIndicatorsCount() {
      if (indicatorsBuilder_ == null) {
        return indicators_.size();
      } else {
        return indicatorsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public de.tum.i13.challenge.Indicator getIndicators(int index) {
      if (indicatorsBuilder_ == null) {
        return indicators_.get(index);
      } else {
        return indicatorsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder setIndicators(
        int index, de.tum.i13.challenge.Indicator value) {
      if (indicatorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIndicatorsIsMutable();
        indicators_.set(index, value);
        onChanged();
      } else {
        indicatorsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder setIndicators(
        int index, de.tum.i13.challenge.Indicator.Builder builderForValue) {
      if (indicatorsBuilder_ == null) {
        ensureIndicatorsIsMutable();
        indicators_.set(index, builderForValue.build());
        onChanged();
      } else {
        indicatorsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder addIndicators(de.tum.i13.challenge.Indicator value) {
      if (indicatorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIndicatorsIsMutable();
        indicators_.add(value);
        onChanged();
      } else {
        indicatorsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder addIndicators(
        int index, de.tum.i13.challenge.Indicator value) {
      if (indicatorsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureIndicatorsIsMutable();
        indicators_.add(index, value);
        onChanged();
      } else {
        indicatorsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder addIndicators(
        de.tum.i13.challenge.Indicator.Builder builderForValue) {
      if (indicatorsBuilder_ == null) {
        ensureIndicatorsIsMutable();
        indicators_.add(builderForValue.build());
        onChanged();
      } else {
        indicatorsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder addIndicators(
        int index, de.tum.i13.challenge.Indicator.Builder builderForValue) {
      if (indicatorsBuilder_ == null) {
        ensureIndicatorsIsMutable();
        indicators_.add(index, builderForValue.build());
        onChanged();
      } else {
        indicatorsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder addAllIndicators(
        java.lang.Iterable<? extends de.tum.i13.challenge.Indicator> values) {
      if (indicatorsBuilder_ == null) {
        ensureIndicatorsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, indicators_);
        onChanged();
      } else {
        indicatorsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder clearIndicators() {
      if (indicatorsBuilder_ == null) {
        indicators_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        indicatorsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public Builder removeIndicators(int index) {
      if (indicatorsBuilder_ == null) {
        ensureIndicatorsIsMutable();
        indicators_.remove(index);
        onChanged();
      } else {
        indicatorsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public de.tum.i13.challenge.Indicator.Builder getIndicatorsBuilder(
        int index) {
      return getIndicatorsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public de.tum.i13.challenge.IndicatorOrBuilder getIndicatorsOrBuilder(
        int index) {
      if (indicatorsBuilder_ == null) {
        return indicators_.get(index);  } else {
        return indicatorsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public java.util.List<? extends de.tum.i13.challenge.IndicatorOrBuilder> 
         getIndicatorsOrBuilderList() {
      if (indicatorsBuilder_ != null) {
        return indicatorsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(indicators_);
      }
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public de.tum.i13.challenge.Indicator.Builder addIndicatorsBuilder() {
      return getIndicatorsFieldBuilder().addBuilder(
          de.tum.i13.challenge.Indicator.getDefaultInstance());
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public de.tum.i13.challenge.Indicator.Builder addIndicatorsBuilder(
        int index) {
      return getIndicatorsFieldBuilder().addBuilder(
          index, de.tum.i13.challenge.Indicator.getDefaultInstance());
    }
    /**
     * <code>repeated .Challenger.Indicator indicators = 3;</code>
     */
    public java.util.List<de.tum.i13.challenge.Indicator.Builder> 
         getIndicatorsBuilderList() {
      return getIndicatorsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.tum.i13.challenge.Indicator, de.tum.i13.challenge.Indicator.Builder, de.tum.i13.challenge.IndicatorOrBuilder> 
        getIndicatorsFieldBuilder() {
      if (indicatorsBuilder_ == null) {
        indicatorsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            de.tum.i13.challenge.Indicator, de.tum.i13.challenge.Indicator.Builder, de.tum.i13.challenge.IndicatorOrBuilder>(
                indicators_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        indicators_ = null;
      }
      return indicatorsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:Challenger.ResultQ1)
  }

  // @@protoc_insertion_point(class_scope:Challenger.ResultQ1)
  private static final de.tum.i13.challenge.ResultQ1 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new de.tum.i13.challenge.ResultQ1();
  }

  public static de.tum.i13.challenge.ResultQ1 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ResultQ1>
      PARSER = new com.google.protobuf.AbstractParser<ResultQ1>() {
    @java.lang.Override
    public ResultQ1 parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ResultQ1(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ResultQ1> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ResultQ1> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public de.tum.i13.challenge.ResultQ1 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

