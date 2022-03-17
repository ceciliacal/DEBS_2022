// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: challenger.proto

package subscription.challenge;

/**
 * Protobuf enum {@code Challenger.Query}
 */
public enum Query
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>Q1 = 0;</code>
   */
  Q1(0),
  /**
   * <code>Q2 = 1;</code>
   */
  Q2(1),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>Q1 = 0;</code>
   */
  public static final int Q1_VALUE = 0;
  /**
   * <code>Q2 = 1;</code>
   */
  public static final int Q2_VALUE = 1;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static Query valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static Query forNumber(int value) {
    switch (value) {
      case 0: return Q1;
      case 1: return Q2;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<Query>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      Query> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<Query>() {
          public Query findValueByNumber(int number) {
            return Query.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return ChallengerProto.getDescriptor().getEnumTypes().get(1);
  }

  private static final Query[] VALUES = values();

  public static Query valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private Query(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:Challenger.Query)
}

