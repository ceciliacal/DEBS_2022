// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/challenger.proto

package de.tum.i13.challenge;

/**
 * Protobuf type {@code Challenger.ResultQ2}
 */
public final class ResultQ2 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Challenger.ResultQ2)
    ResultQ2OrBuilder {
private static final long serialVersionUID = 0L;
  // Use ResultQ2.newBuilder() to construct.
  private ResultQ2(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ResultQ2() {
    crossoverEvents_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ResultQ2();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ResultQ2(
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
              crossoverEvents_ = new java.util.ArrayList<de.tum.i13.challenge.CrossoverEvent>();
              mutable_bitField0_ |= 0x00000001;
            }
            crossoverEvents_.add(
                input.readMessage(de.tum.i13.challenge.CrossoverEvent.parser(), extensionRegistry));
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
        crossoverEvents_ = java.util.Collections.unmodifiableList(crossoverEvents_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ2_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ2_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            de.tum.i13.challenge.ResultQ2.class, de.tum.i13.challenge.ResultQ2.Builder.class);
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

  public static final int CROSSOVER_EVENTS_FIELD_NUMBER = 3;
  private java.util.List<de.tum.i13.challenge.CrossoverEvent> crossoverEvents_;
  /**
   * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
   */
  @java.lang.Override
  public java.util.List<de.tum.i13.challenge.CrossoverEvent> getCrossoverEventsList() {
    return crossoverEvents_;
  }
  /**
   * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
   */
  @java.lang.Override
  public java.util.List<? extends de.tum.i13.challenge.CrossoverEventOrBuilder> 
      getCrossoverEventsOrBuilderList() {
    return crossoverEvents_;
  }
  /**
   * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
   */
  @java.lang.Override
  public int getCrossoverEventsCount() {
    return crossoverEvents_.size();
  }
  /**
   * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
   */
  @java.lang.Override
  public de.tum.i13.challenge.CrossoverEvent getCrossoverEvents(int index) {
    return crossoverEvents_.get(index);
  }
  /**
   * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
   */
  @java.lang.Override
  public de.tum.i13.challenge.CrossoverEventOrBuilder getCrossoverEventsOrBuilder(
      int index) {
    return crossoverEvents_.get(index);
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
    for (int i = 0; i < crossoverEvents_.size(); i++) {
      output.writeMessage(3, crossoverEvents_.get(i));
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
    for (int i = 0; i < crossoverEvents_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, crossoverEvents_.get(i));
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
    if (!(obj instanceof de.tum.i13.challenge.ResultQ2)) {
      return super.equals(obj);
    }
    de.tum.i13.challenge.ResultQ2 other = (de.tum.i13.challenge.ResultQ2) obj;

    if (getBenchmarkId()
        != other.getBenchmarkId()) return false;
    if (getBatchSeqId()
        != other.getBatchSeqId()) return false;
    if (!getCrossoverEventsList()
        .equals(other.getCrossoverEventsList())) return false;
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
    if (getCrossoverEventsCount() > 0) {
      hash = (37 * hash) + CROSSOVER_EVENTS_FIELD_NUMBER;
      hash = (53 * hash) + getCrossoverEventsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ2 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ2 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static de.tum.i13.challenge.ResultQ2 parseFrom(
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
  public static Builder newBuilder(de.tum.i13.challenge.ResultQ2 prototype) {
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
   * Protobuf type {@code Challenger.ResultQ2}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Challenger.ResultQ2)
      de.tum.i13.challenge.ResultQ2OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ2_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ2_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              de.tum.i13.challenge.ResultQ2.class, de.tum.i13.challenge.ResultQ2.Builder.class);
    }

    // Construct using de.tum.i13.challenge.ResultQ2.newBuilder()
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
        getCrossoverEventsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      benchmarkId_ = 0L;

      batchSeqId_ = 0L;

      if (crossoverEventsBuilder_ == null) {
        crossoverEvents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        crossoverEventsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return de.tum.i13.challenge.ChallengerProto.internal_static_Challenger_ResultQ2_descriptor;
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ2 getDefaultInstanceForType() {
      return de.tum.i13.challenge.ResultQ2.getDefaultInstance();
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ2 build() {
      de.tum.i13.challenge.ResultQ2 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public de.tum.i13.challenge.ResultQ2 buildPartial() {
      de.tum.i13.challenge.ResultQ2 result = new de.tum.i13.challenge.ResultQ2(this);
      int from_bitField0_ = bitField0_;
      result.benchmarkId_ = benchmarkId_;
      result.batchSeqId_ = batchSeqId_;
      if (crossoverEventsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          crossoverEvents_ = java.util.Collections.unmodifiableList(crossoverEvents_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.crossoverEvents_ = crossoverEvents_;
      } else {
        result.crossoverEvents_ = crossoverEventsBuilder_.build();
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
      if (other instanceof de.tum.i13.challenge.ResultQ2) {
        return mergeFrom((de.tum.i13.challenge.ResultQ2)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(de.tum.i13.challenge.ResultQ2 other) {
      if (other == de.tum.i13.challenge.ResultQ2.getDefaultInstance()) return this;
      if (other.getBenchmarkId() != 0L) {
        setBenchmarkId(other.getBenchmarkId());
      }
      if (other.getBatchSeqId() != 0L) {
        setBatchSeqId(other.getBatchSeqId());
      }
      if (crossoverEventsBuilder_ == null) {
        if (!other.crossoverEvents_.isEmpty()) {
          if (crossoverEvents_.isEmpty()) {
            crossoverEvents_ = other.crossoverEvents_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureCrossoverEventsIsMutable();
            crossoverEvents_.addAll(other.crossoverEvents_);
          }
          onChanged();
        }
      } else {
        if (!other.crossoverEvents_.isEmpty()) {
          if (crossoverEventsBuilder_.isEmpty()) {
            crossoverEventsBuilder_.dispose();
            crossoverEventsBuilder_ = null;
            crossoverEvents_ = other.crossoverEvents_;
            bitField0_ = (bitField0_ & ~0x00000001);
            crossoverEventsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getCrossoverEventsFieldBuilder() : null;
          } else {
            crossoverEventsBuilder_.addAllMessages(other.crossoverEvents_);
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
      de.tum.i13.challenge.ResultQ2 parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (de.tum.i13.challenge.ResultQ2) e.getUnfinishedMessage();
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

    private java.util.List<de.tum.i13.challenge.CrossoverEvent> crossoverEvents_ =
      java.util.Collections.emptyList();
    private void ensureCrossoverEventsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        crossoverEvents_ = new java.util.ArrayList<de.tum.i13.challenge.CrossoverEvent>(crossoverEvents_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.tum.i13.challenge.CrossoverEvent, de.tum.i13.challenge.CrossoverEvent.Builder, de.tum.i13.challenge.CrossoverEventOrBuilder> crossoverEventsBuilder_;

    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public java.util.List<de.tum.i13.challenge.CrossoverEvent> getCrossoverEventsList() {
      if (crossoverEventsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(crossoverEvents_);
      } else {
        return crossoverEventsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public int getCrossoverEventsCount() {
      if (crossoverEventsBuilder_ == null) {
        return crossoverEvents_.size();
      } else {
        return crossoverEventsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public de.tum.i13.challenge.CrossoverEvent getCrossoverEvents(int index) {
      if (crossoverEventsBuilder_ == null) {
        return crossoverEvents_.get(index);
      } else {
        return crossoverEventsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder setCrossoverEvents(
        int index, de.tum.i13.challenge.CrossoverEvent value) {
      if (crossoverEventsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.set(index, value);
        onChanged();
      } else {
        crossoverEventsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder setCrossoverEvents(
        int index, de.tum.i13.challenge.CrossoverEvent.Builder builderForValue) {
      if (crossoverEventsBuilder_ == null) {
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.set(index, builderForValue.build());
        onChanged();
      } else {
        crossoverEventsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder addCrossoverEvents(de.tum.i13.challenge.CrossoverEvent value) {
      if (crossoverEventsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.add(value);
        onChanged();
      } else {
        crossoverEventsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder addCrossoverEvents(
        int index, de.tum.i13.challenge.CrossoverEvent value) {
      if (crossoverEventsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.add(index, value);
        onChanged();
      } else {
        crossoverEventsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder addCrossoverEvents(
        de.tum.i13.challenge.CrossoverEvent.Builder builderForValue) {
      if (crossoverEventsBuilder_ == null) {
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.add(builderForValue.build());
        onChanged();
      } else {
        crossoverEventsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder addCrossoverEvents(
        int index, de.tum.i13.challenge.CrossoverEvent.Builder builderForValue) {
      if (crossoverEventsBuilder_ == null) {
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.add(index, builderForValue.build());
        onChanged();
      } else {
        crossoverEventsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder addAllCrossoverEvents(
        java.lang.Iterable<? extends de.tum.i13.challenge.CrossoverEvent> values) {
      if (crossoverEventsBuilder_ == null) {
        ensureCrossoverEventsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, crossoverEvents_);
        onChanged();
      } else {
        crossoverEventsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder clearCrossoverEvents() {
      if (crossoverEventsBuilder_ == null) {
        crossoverEvents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        crossoverEventsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public Builder removeCrossoverEvents(int index) {
      if (crossoverEventsBuilder_ == null) {
        ensureCrossoverEventsIsMutable();
        crossoverEvents_.remove(index);
        onChanged();
      } else {
        crossoverEventsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public de.tum.i13.challenge.CrossoverEvent.Builder getCrossoverEventsBuilder(
        int index) {
      return getCrossoverEventsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public de.tum.i13.challenge.CrossoverEventOrBuilder getCrossoverEventsOrBuilder(
        int index) {
      if (crossoverEventsBuilder_ == null) {
        return crossoverEvents_.get(index);  } else {
        return crossoverEventsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public java.util.List<? extends de.tum.i13.challenge.CrossoverEventOrBuilder> 
         getCrossoverEventsOrBuilderList() {
      if (crossoverEventsBuilder_ != null) {
        return crossoverEventsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(crossoverEvents_);
      }
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public de.tum.i13.challenge.CrossoverEvent.Builder addCrossoverEventsBuilder() {
      return getCrossoverEventsFieldBuilder().addBuilder(
          de.tum.i13.challenge.CrossoverEvent.getDefaultInstance());
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public de.tum.i13.challenge.CrossoverEvent.Builder addCrossoverEventsBuilder(
        int index) {
      return getCrossoverEventsFieldBuilder().addBuilder(
          index, de.tum.i13.challenge.CrossoverEvent.getDefaultInstance());
    }
    /**
     * <code>repeated .Challenger.CrossoverEvent crossover_events = 3;</code>
     */
    public java.util.List<de.tum.i13.challenge.CrossoverEvent.Builder> 
         getCrossoverEventsBuilderList() {
      return getCrossoverEventsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        de.tum.i13.challenge.CrossoverEvent, de.tum.i13.challenge.CrossoverEvent.Builder, de.tum.i13.challenge.CrossoverEventOrBuilder> 
        getCrossoverEventsFieldBuilder() {
      if (crossoverEventsBuilder_ == null) {
        crossoverEventsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            de.tum.i13.challenge.CrossoverEvent, de.tum.i13.challenge.CrossoverEvent.Builder, de.tum.i13.challenge.CrossoverEventOrBuilder>(
                crossoverEvents_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        crossoverEvents_ = null;
      }
      return crossoverEventsBuilder_;
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


    // @@protoc_insertion_point(builder_scope:Challenger.ResultQ2)
  }

  // @@protoc_insertion_point(class_scope:Challenger.ResultQ2)
  private static final de.tum.i13.challenge.ResultQ2 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new de.tum.i13.challenge.ResultQ2();
  }

  public static de.tum.i13.challenge.ResultQ2 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ResultQ2>
      PARSER = new com.google.protobuf.AbstractParser<ResultQ2>() {
    @java.lang.Override
    public ResultQ2 parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ResultQ2(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ResultQ2> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ResultQ2> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public de.tum.i13.challenge.ResultQ2 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

