package de.tum.i13.challenge;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.1)",
    comments = "Source: challenger.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChallengerGrpc {

  private ChallengerGrpc() {}

  public static final String SERVICE_NAME = "Challenger.Challenger";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.BenchmarkConfiguration,
      de.tum.i13.challenge.Benchmark> getCreateNewBenchmarkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "createNewBenchmark",
      requestType = de.tum.i13.challenge.BenchmarkConfiguration.class,
      responseType = de.tum.i13.challenge.Benchmark.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.BenchmarkConfiguration,
      de.tum.i13.challenge.Benchmark> getCreateNewBenchmarkMethod() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.BenchmarkConfiguration, de.tum.i13.challenge.Benchmark> getCreateNewBenchmarkMethod;
    if ((getCreateNewBenchmarkMethod = ChallengerGrpc.getCreateNewBenchmarkMethod) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getCreateNewBenchmarkMethod = ChallengerGrpc.getCreateNewBenchmarkMethod) == null) {
          ChallengerGrpc.getCreateNewBenchmarkMethod = getCreateNewBenchmarkMethod =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.BenchmarkConfiguration, de.tum.i13.challenge.Benchmark>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "createNewBenchmark"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.BenchmarkConfiguration.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.Benchmark.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("createNewBenchmark"))
              .build();
        }
      }
    }
    return getCreateNewBenchmarkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      com.google.protobuf.Empty> getStartBenchmarkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "startBenchmark",
      requestType = de.tum.i13.challenge.Benchmark.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      com.google.protobuf.Empty> getStartBenchmarkMethod() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark, com.google.protobuf.Empty> getStartBenchmarkMethod;
    if ((getStartBenchmarkMethod = ChallengerGrpc.getStartBenchmarkMethod) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getStartBenchmarkMethod = ChallengerGrpc.getStartBenchmarkMethod) == null) {
          ChallengerGrpc.getStartBenchmarkMethod = getStartBenchmarkMethod =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.Benchmark, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "startBenchmark"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.Benchmark.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("startBenchmark"))
              .build();
        }
      }
    }
    return getStartBenchmarkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      de.tum.i13.challenge.Batch> getNextBatchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "nextBatch",
      requestType = de.tum.i13.challenge.Benchmark.class,
      responseType = de.tum.i13.challenge.Batch.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      de.tum.i13.challenge.Batch> getNextBatchMethod() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark, de.tum.i13.challenge.Batch> getNextBatchMethod;
    if ((getNextBatchMethod = ChallengerGrpc.getNextBatchMethod) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getNextBatchMethod = ChallengerGrpc.getNextBatchMethod) == null) {
          ChallengerGrpc.getNextBatchMethod = getNextBatchMethod =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.Benchmark, de.tum.i13.challenge.Batch>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "nextBatch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.Benchmark.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.Batch.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("nextBatch"))
              .build();
        }
      }
    }
    return getNextBatchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ1,
      com.google.protobuf.Empty> getResultQ1Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "resultQ1",
      requestType = de.tum.i13.challenge.ResultQ1.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ1,
      com.google.protobuf.Empty> getResultQ1Method() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ1, com.google.protobuf.Empty> getResultQ1Method;
    if ((getResultQ1Method = ChallengerGrpc.getResultQ1Method) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getResultQ1Method = ChallengerGrpc.getResultQ1Method) == null) {
          ChallengerGrpc.getResultQ1Method = getResultQ1Method =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.ResultQ1, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "resultQ1"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.ResultQ1.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("resultQ1"))
              .build();
        }
      }
    }
    return getResultQ1Method;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ2,
      com.google.protobuf.Empty> getResultQ2Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "resultQ2",
      requestType = de.tum.i13.challenge.ResultQ2.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ2,
      com.google.protobuf.Empty> getResultQ2Method() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.ResultQ2, com.google.protobuf.Empty> getResultQ2Method;
    if ((getResultQ2Method = ChallengerGrpc.getResultQ2Method) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getResultQ2Method = ChallengerGrpc.getResultQ2Method) == null) {
          ChallengerGrpc.getResultQ2Method = getResultQ2Method =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.ResultQ2, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "resultQ2"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.ResultQ2.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("resultQ2"))
              .build();
        }
      }
    }
    return getResultQ2Method;
  }

  private static volatile io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      com.google.protobuf.Empty> getEndBenchmarkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "endBenchmark",
      requestType = de.tum.i13.challenge.Benchmark.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark,
      com.google.protobuf.Empty> getEndBenchmarkMethod() {
    io.grpc.MethodDescriptor<de.tum.i13.challenge.Benchmark, com.google.protobuf.Empty> getEndBenchmarkMethod;
    if ((getEndBenchmarkMethod = ChallengerGrpc.getEndBenchmarkMethod) == null) {
      synchronized (ChallengerGrpc.class) {
        if ((getEndBenchmarkMethod = ChallengerGrpc.getEndBenchmarkMethod) == null) {
          ChallengerGrpc.getEndBenchmarkMethod = getEndBenchmarkMethod =
              io.grpc.MethodDescriptor.<de.tum.i13.challenge.Benchmark, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "endBenchmark"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  de.tum.i13.challenge.Benchmark.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChallengerMethodDescriptorSupplier("endBenchmark"))
              .build();
        }
      }
    }
    return getEndBenchmarkMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChallengerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChallengerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChallengerStub>() {
        @java.lang.Override
        public ChallengerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChallengerStub(channel, callOptions);
        }
      };
    return ChallengerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChallengerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChallengerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChallengerBlockingStub>() {
        @java.lang.Override
        public ChallengerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChallengerBlockingStub(channel, callOptions);
        }
      };
    return ChallengerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChallengerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChallengerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChallengerFutureStub>() {
        @java.lang.Override
        public ChallengerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChallengerFutureStub(channel, callOptions);
        }
      };
    return ChallengerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ChallengerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Create a new Benchmark based on the configuration
     * </pre>
     */
    public void createNewBenchmark(de.tum.i13.challenge.BenchmarkConfiguration request,
        io.grpc.stub.StreamObserver<de.tum.i13.challenge.Benchmark> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateNewBenchmarkMethod(), responseObserver);
    }

    /**
     * <pre>
     *This marks the starting point of the throughput measurements
     * </pre>
     */
    public void startBenchmark(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartBenchmarkMethod(), responseObserver);
    }

    /**
     * <pre>
     *get the next Batch
     * </pre>
     */
    public void nextBatch(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<de.tum.i13.challenge.Batch> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNextBatchMethod(), responseObserver);
    }

    /**
     * <pre>
     *post the result
     * </pre>
     */
    public void resultQ1(de.tum.i13.challenge.ResultQ1 request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getResultQ1Method(), responseObserver);
    }

    /**
     */
    public void resultQ2(de.tum.i13.challenge.ResultQ2 request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getResultQ2Method(), responseObserver);
    }

    /**
     * <pre>
     *This marks the end of the throughput measurements
     * </pre>
     */
    public void endBenchmark(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEndBenchmarkMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreateNewBenchmarkMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.BenchmarkConfiguration,
                de.tum.i13.challenge.Benchmark>(
                  this, METHODID_CREATE_NEW_BENCHMARK)))
          .addMethod(
            getStartBenchmarkMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.Benchmark,
                com.google.protobuf.Empty>(
                  this, METHODID_START_BENCHMARK)))
          .addMethod(
            getNextBatchMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.Benchmark,
                de.tum.i13.challenge.Batch>(
                  this, METHODID_NEXT_BATCH)))
          .addMethod(
            getResultQ1Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.ResultQ1,
                com.google.protobuf.Empty>(
                  this, METHODID_RESULT_Q1)))
          .addMethod(
            getResultQ2Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.ResultQ2,
                com.google.protobuf.Empty>(
                  this, METHODID_RESULT_Q2)))
          .addMethod(
            getEndBenchmarkMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                de.tum.i13.challenge.Benchmark,
                com.google.protobuf.Empty>(
                  this, METHODID_END_BENCHMARK)))
          .build();
    }
  }

  /**
   */
  public static final class ChallengerStub extends io.grpc.stub.AbstractAsyncStub<ChallengerStub> {
    private ChallengerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChallengerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChallengerStub(channel, callOptions);
    }

    /**
     * <pre>
     *Create a new Benchmark based on the configuration
     * </pre>
     */
    public void createNewBenchmark(de.tum.i13.challenge.BenchmarkConfiguration request,
        io.grpc.stub.StreamObserver<de.tum.i13.challenge.Benchmark> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateNewBenchmarkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *This marks the starting point of the throughput measurements
     * </pre>
     */
    public void startBenchmark(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartBenchmarkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *get the next Batch
     * </pre>
     */
    public void nextBatch(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<de.tum.i13.challenge.Batch> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNextBatchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *post the result
     * </pre>
     */
    public void resultQ1(de.tum.i13.challenge.ResultQ1 request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getResultQ1Method(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void resultQ2(de.tum.i13.challenge.ResultQ2 request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getResultQ2Method(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *This marks the end of the throughput measurements
     * </pre>
     */
    public void endBenchmark(de.tum.i13.challenge.Benchmark request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEndBenchmarkMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ChallengerBlockingStub extends io.grpc.stub.AbstractBlockingStub<ChallengerBlockingStub> {
    private ChallengerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChallengerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChallengerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *Create a new Benchmark based on the configuration
     * </pre>
     */
    public de.tum.i13.challenge.Benchmark createNewBenchmark(de.tum.i13.challenge.BenchmarkConfiguration request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateNewBenchmarkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *This marks the starting point of the throughput measurements
     * </pre>
     */
    public com.google.protobuf.Empty startBenchmark(de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartBenchmarkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *get the next Batch
     * </pre>
     */
    public de.tum.i13.challenge.Batch nextBatch(de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNextBatchMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *post the result
     * </pre>
     */
    public com.google.protobuf.Empty resultQ1(de.tum.i13.challenge.ResultQ1 request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getResultQ1Method(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty resultQ2(de.tum.i13.challenge.ResultQ2 request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getResultQ2Method(), getCallOptions(), request);
    }

    /**
     * <pre>
     *This marks the end of the throughput measurements
     * </pre>
     */
    public com.google.protobuf.Empty endBenchmark(de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEndBenchmarkMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ChallengerFutureStub extends io.grpc.stub.AbstractFutureStub<ChallengerFutureStub> {
    private ChallengerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChallengerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChallengerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *Create a new Benchmark based on the configuration
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.i13.challenge.Benchmark> createNewBenchmark(
        de.tum.i13.challenge.BenchmarkConfiguration request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateNewBenchmarkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *This marks the starting point of the throughput measurements
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> startBenchmark(
        de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartBenchmarkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *get the next Batch
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<de.tum.i13.challenge.Batch> nextBatch(
        de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNextBatchMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *post the result
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> resultQ1(
        de.tum.i13.challenge.ResultQ1 request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getResultQ1Method(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> resultQ2(
        de.tum.i13.challenge.ResultQ2 request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getResultQ2Method(), getCallOptions()), request);
    }

    /**
     * <pre>
     *This marks the end of the throughput measurements
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> endBenchmark(
        de.tum.i13.challenge.Benchmark request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEndBenchmarkMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_NEW_BENCHMARK = 0;
  private static final int METHODID_START_BENCHMARK = 1;
  private static final int METHODID_NEXT_BATCH = 2;
  private static final int METHODID_RESULT_Q1 = 3;
  private static final int METHODID_RESULT_Q2 = 4;
  private static final int METHODID_END_BENCHMARK = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ChallengerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChallengerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_NEW_BENCHMARK:
          serviceImpl.createNewBenchmark((de.tum.i13.challenge.BenchmarkConfiguration) request,
              (io.grpc.stub.StreamObserver<de.tum.i13.challenge.Benchmark>) responseObserver);
          break;
        case METHODID_START_BENCHMARK:
          serviceImpl.startBenchmark((de.tum.i13.challenge.Benchmark) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_NEXT_BATCH:
          serviceImpl.nextBatch((de.tum.i13.challenge.Benchmark) request,
              (io.grpc.stub.StreamObserver<de.tum.i13.challenge.Batch>) responseObserver);
          break;
        case METHODID_RESULT_Q1:
          serviceImpl.resultQ1((de.tum.i13.challenge.ResultQ1) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_RESULT_Q2:
          serviceImpl.resultQ2((de.tum.i13.challenge.ResultQ2) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_END_BENCHMARK:
          serviceImpl.endBenchmark((de.tum.i13.challenge.Benchmark) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ChallengerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChallengerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return de.tum.i13.challenge.ChallengerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Challenger");
    }
  }

  private static final class ChallengerFileDescriptorSupplier
      extends ChallengerBaseDescriptorSupplier {
    ChallengerFileDescriptorSupplier() {}
  }

  private static final class ChallengerMethodDescriptorSupplier
      extends ChallengerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ChallengerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChallengerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChallengerFileDescriptorSupplier())
              .addMethod(getCreateNewBenchmarkMethod())
              .addMethod(getStartBenchmarkMethod())
              .addMethod(getNextBatchMethod())
              .addMethod(getResultQ1Method())
              .addMethod(getResultQ2Method())
              .addMethod(getEndBenchmarkMethod())
              .build();
        }
      }
    }
    return result;
  }
}
