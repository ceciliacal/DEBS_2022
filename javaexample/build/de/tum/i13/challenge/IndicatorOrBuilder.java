// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/challenger.proto

package de.tum.i13.challenge;

public interface IndicatorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Challenger.Indicator)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string symbol = 1;</code>
   * @return The symbol.
   */
  java.lang.String getSymbol();
  /**
   * <code>string symbol = 1;</code>
   * @return The bytes for symbol.
   */
  com.google.protobuf.ByteString
      getSymbolBytes();

  /**
   * <code>float ema_38 = 2;</code>
   * @return The ema38.
   */
  float getEma38();

  /**
   * <code>float ema_100 = 3;</code>
   * @return The ema100.
   */
  float getEma100();
}
