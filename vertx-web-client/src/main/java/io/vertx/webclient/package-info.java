/**
 * = Vert.x Web Client
 *
 * Vert.x Web Client is an asynchronous HTTP and HTTP/2 client.
 *
 * The Web Client makes easy to do HTTP request/response interactions with a web server, and provides advanced
 * features like:
 *
 * * Json body encoding / decoding
 * * request/response pumping
 * * request parameters
 * * unified error handling
 * * form submissions
 *
 * The web client does not deprecate the Vert.x Core {@link io.vertx.core.http.HttpClient}, indeed it is based on
 * this client and inherits its configuration and great features like pooling, HTTP/2 support, pipelining support, etc...
 * The {@link io.vertx.core.http.HttpClient} should be used when fine grained control over the HTTP
 * requests/response is necessary.
 *
 * == Using the web client
 *
 * To use Vert.x Web Client, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * dependencies {
 *   compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * }
 * ----
 *
 * == Re-cap on Vert.x core HTTP client
 *
 * Vert.x Web Client uses the API from Vert.x core, so it's well worth getting familiar with the basic concepts of using
 * {@link io.vertx.core.http.HttpClient} using Vert.x core, if you're not already.
 *
 * == Creating a web client
 *
 * You create an {@link io.vertx.webclient.WebClient} instance with default options as follows
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#create}
 * ----
 *
 * If you want to configure options for the client, you create it as follows
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#createFromOptions}
 * ----
 *
 * == Making requests
 *
 * === Simple requests with no body
 *
 * Often, you’ll want to make HTTP requests with no request body. This is usually the case with HTTP GET, OPTIONS
 * and HEAD requests
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#simpleGetAndHead}
 * ----
 *
 * You can add query parameters to the request URI in a fluent fashion
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#simpleGetWithParams(io.vertx.webclient.WebClient)}
 * ----
 *
 * Any request URI parameter will pre-populate the request
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#simpleGetWithInitialParams(io.vertx.webclient.WebClient)}
 * ----
 *
 * Setting a request URI discards existing query parameters.
 *
 * === Writing request bodies
 *
 * When you need to make a request with a body, you use the same API and call then `sendXXX` methods
 * that expects a body to send.
 *
 * Use {@link io.vertx.webclient.HttpRequest#sendBuffer} to send a buffer body
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendBuffer(io.vertx.webclient.WebClient, io.vertx.core.buffer.Buffer)}
 * ----
 *
 * Sending a single buffer is useful but often you don't want to load fully the content in memory because
 * it may be too large or you want to handle many concurrent requests and want to use just the minimum
 * for each request. For this purpose the web client can send `ReadStream<Buffer>` (e.g a
 * {@link io.vertx.core.file.AsyncFile} is a ReadStream<Buffer>`) with the {@link io.vertx.webclient.HttpRequest#sendStream} method
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendStreamChunked(io.vertx.webclient.WebClient, io.vertx.core.streams.ReadStream)}
 * ----
 *
 * The web client takes care of setting up the transfer pump for you. Since the length of the stream is not know
 * the request will use chunked transfer encoding .
 *
 * When you know the size of the stream, you shall specify before using the `content-length` header
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendStream(io.vertx.webclient.WebClient, io.vertx.core.file.FileSystem)}
 * ----
 *
 * The POST will not be chunked.
 *
 * ==== Json bodies
 *
 * Often you’ll want to send Json body requests, to send a {@link io.vertx.core.json.JsonObject}
 * use the {@link io.vertx.webclient.HttpRequest#sendJsonObject(io.vertx.core.json.JsonObject, io.vertx.core.Handler)}
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendJsonObject(io.vertx.webclient.WebClient)}
 * ----
 *
 * In Java, Groovy or Kotlin, you can use the {@link io.vertx.webclient.HttpRequest#sendJson} method that maps
 * a POJO (Plain Old Java Object) to a Json object using {@link io.vertx.core.json.Json#encode(java.lang.Object)}
 * method
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendJsonPOJO(io.vertx.webclient.WebClient)}
 * ----
 *
 * NOTE: the {@link io.vertx.core.json.Json#encode(java.lang.Object)} uses the Jackson mapper to encode the object
 * to Json.
 *
 * ==== Form submissions
 *
 * You can send http form submissions bodies with the {@link io.vertx.webclient.HttpRequest#sendForm(io.vertx.core.MultiMap, io.vertx.core.Handler)}
 * variant.
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendForm(io.vertx.webclient.WebClient)}
 * ----
 *
 * By default the form is submitted with the `application/x-www-form-urlencoded` content type header. You can set
 * the `content-type` header to `multipart/form-data` instead
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendMultipart(io.vertx.webclient.WebClient)}
 * ----
 *
 * NOTE: at the moment multipart files are not supported, it will likely be supported in a later revision
 * of the API.
 *
 * === Writing request headers
 *
 * You can write headers to a request using the headers multi-map as follows:
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendHeaders1(io.vertx.webclient.WebClient)}
 * ----
 *
 * The headers are an instance of {@link io.vertx.core.MultiMap} which provides operations for adding,
 * setting and removing entries. Http headers allow more than one value for a specific key.
 *
 * You can also write headers using putHeader
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#sendHeaders2(io.vertx.webclient.WebClient)}
 * ----
 *
 * === Reusing requests
 *
 * The {@link io.vertx.webclient.HttpRequest#send(io.vertx.core.Handler)} method can be called multiple times
 * safely, making it very easy to configure and reuse {@link io.vertx.webclient.HttpRequest} objects
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#multiGet(io.vertx.webclient.WebClient)}
 * ----
 *
 * When you need to mutate a request, the {@link io.vertx.webclient.HttpRequest#copy()} returns a copy of the
 * request
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#multiGetCopy(io.vertx.webclient.WebClient)}
 * ----
 *
 * == Handling http responses
 *
 * When the web client sends a request you always deal with a single async result {@link io.vertx.webclient.HttpResponse}.
 *
 * On a success result the callback happens after the response has been received
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponse(io.vertx.webclient.WebClient)}
 * ----
 *
 * === Decoding responses
 *
 * By default the web client provides an http response body as a {@code Buffer} and does not apply
 * any decoding.
 *
 * Custom response body decoding can be achieved using {@link io.vertx.webclient.BodyCodec}:
 *
 * * Plain String
 * * Json object
 * * Json mapped POJO
 * * {@link io.vertx.core.streams.WriteStream}
 *
 * A body codec can decode an arbitrary binary data stream into a specific object instance, saving you the decoding
 * step in your response handlers.
 *
 * Use {@link io.vertx.webclient.BodyCodec#jsonObject()} To decode a Json object:
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponseAsJsonObject(io.vertx.webclient.WebClient)}
 * ----
 *
 * In Java, Groovy or Kotlin, custom Json mapped POJO can be decoded
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponseAsJsonPOJO(io.vertx.webclient.WebClient)}
 * ----
 *
 * When large response are expected, use the {@link io.vertx.webclient.BodyCodec#writeStream(io.vertx.core.streams.WriteStream)}.
 * This body codec pumps the response body buffers to a {@link io.vertx.core.streams.WriteStream}
 * and signals the success or the failure of the operation in the async result response
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponseAsWriteStream(io.vertx.webclient.WebClient, io.vertx.core.streams.WriteStream)}
 * ----
 *
 * Finally if you are not interested at all by the response content, the {@link io.vertx.webclient.BodyCodec#none()}
 * simply discards the entire response body
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponseAndDiscard(io.vertx.webclient.WebClient)}
 * ----
 *
 * When you don't know in advance the content type of the http response, you can still use the {@code bodyAsXXX()} methods
 * that decode the response to a specific type
 *
 * [source,java]
 * ----
 * {@link examples.WebClientExamples#receiveResponseAsBufferDecodeAsJsonObject(io.vertx.webclient.WebClient)}
 * ----
 *
 * WARNING: this is only valid for the response decoded as a buffer.
 *
 * === RxJava API
 *
 * The RxJava {@link io.vertx.rxjava.webclient.HttpRequest} provides an rx-ified version of the original API,
 * the {@link io.vertx.rxjava.webclient.HttpRequest#rxSend()} method returns a `Single<HttpResponse<Buffer>>` that
 * makes the HTTP request upon subscription, as consequence, the {@code Single} can be subscribed many times.
 *
 * [source,java]
 * ----
 * {@link examples.RxWebClientExamples#simpleGet(io.vertx.rxjava.webclient.WebClient)}
 * ----
 *
 * The obtained {@code Single} can be composed and chained naturally with the RxJava API
 *
 * [source,java]
 * ----
 * {@link examples.RxWebClientExamples#flatMap(io.vertx.rxjava.webclient.WebClient)}
 * ----
 *
 * The same APIs is available
 *
 * [source,java]
 * ----
 * {@link examples.RxWebClientExamples#moreComplex(io.vertx.rxjava.webclient.WebClient)}
 * ----
 *
 * The {@link io.vertx.rxjava.webclient.HttpRequest#sendStream(rx.Observable, io.vertx.core.Handler)} shall
 * be preferred for sending bodies {@code Observable<Buffer>}
 *
 * [source,java]
 * ----
 * {@link examples.RxWebClientExamples#sendObservable(io.vertx.rxjava.webclient.WebClient)}
 * ----
 *
 * Upon subscription, the {@code body} will be subscribed and its content used for the request.
 */
@Document(fileName = "index.adoc")
@ModuleGen(name = "vertx-web-client", groupPackage = "io.vertx")
package io.vertx.webclient;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
