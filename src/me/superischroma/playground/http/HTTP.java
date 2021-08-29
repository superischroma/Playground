package me.superischroma.playground.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.superischroma.playground.util.Promise;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletionException;

public final class HTTP
{
    /**
     * Asynchronously makes an HTTP request to the specified URL with the provided request method and parameters
     * @param url The root URL to make a call to; Do NOT provide a query in this field
     * @param requestMethod The method of the request to be made
     * @param parameters Parameters for this request
     * @param headers Headers for this request
     * @return A CompletableFuture which, when resolved, will provide the response sent back by the server
     */
    public static Promise<Response> request(String url, RequestMethod requestMethod, Parameters parameters, Headers headers)
    {
        return new Promise<>(() ->
        {
            URI uri;
            try
            {
                uri = new URI(url);
            }
            catch (URISyntaxException ex)
            {
                throw new CompletionException(ex);
            }
            String query = uri.getQuery();
            if (query != null)
                throw new CompletionException(new MalformedURLException("Include request parameters in the " + Parameters.class.getSimpleName() + " object, not in the URL string"));
            if (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))
                throw new CompletionException(new MalformedURLException("Cannot make an HTTP request to a URL with the scheme \"" + uri.getScheme() + "\""));
            try
            {
                uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), parameters.toString(), uri.getFragment());
            }
            catch (URISyntaxException ex)
            {
                ex.printStackTrace();
            }
            HttpURLConnection connection;
            Response response;
            byte[] buffer = null;
            try
            {
                connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod(requestMethod.name());
                for (Map.Entry<String, Object> header : headers)
                    connection.setRequestProperty(header.getKey(), header.getValue().toString());
                if (requestMethod == RequestMethod.GET || requestMethod == RequestMethod.OPTIONS || requestMethod == RequestMethod.TRACE)
                {
                    InputStream stream;
                    if (connection.getResponseCode() >= 400)
                        stream = connection.getErrorStream();
                    else
                        stream = connection.getInputStream();
                    buffer = stream.readAllBytes();
                    stream.close();
                }
                response = new Response(buffer, connection.getResponseCode(), connection.getResponseMessage(), connection.getContentType());
            }
            catch (IOException ex)
            {
                throw new CompletionException(ex);
            }
            connection.disconnect();
            return response;
        });
    }

    public static Promise<Response> request(String url, RequestMethod requestMethod)
    {
        return request(url, requestMethod, new Parameters(), new Headers());
    }

    public static Promise<Response> request(String url, Parameters parameters)
    {
        return request(url, RequestMethod.GET, parameters, new Headers());
    }

    public static Promise<Response> request(String url)
    {
        return request(url, RequestMethod.GET, new Parameters(), new Headers());
    }

    /**
     * Synchronously makes an HTTP request to the specified URL with the provided request method and parameters
     * @param url The root URL to make a call to; Do NOT provide a query in this field
     * @param requestMethod The method of the request to be made
     * @param parameters Parameters for this request
     * @return  The response sent back by the server
     */
    public static Response requestSync(String url, RequestMethod requestMethod, Parameters parameters, Headers headers)
    {
        return request(url, requestMethod, parameters, headers).await();
    }

    public static Response requestSync(String url, RequestMethod requestMethod)
    {
        return requestSync(url, requestMethod, new Parameters(), new Headers());
    }

    public static Response requestSync(String url, Parameters parameters)
    {
        return requestSync(url, RequestMethod.GET, parameters, new Headers());
    }

    public static Response requestSync(String url)
    {
        return requestSync(url, RequestMethod.GET, new Parameters(), new Headers());
    }

    public static class Response
    {
        private byte[] buffer;
        private final int code;
        private final String message;
        private final ContentType contentType;

        private Response(byte[] buffer, int code, String message, String contentType)
        {
            if (buffer != null)
                this.buffer = buffer;
            this.code = code;
            this.message = message;
            this.contentType = new ContentType(contentType);
        }

        /**
         * Gets this response's body as JSON
         * @return A GSON JsonElement containing this response's body as JSON
         * @throws ResponseException If response has no body or incorrect Content-Type
         */
        public JsonElement json() throws ResponseException
        {
            if (buffer == null)
                throw new ResponseException("Response has no body");
            if (!contentType.mediaType.equals("application/json"))
                throw new ResponseException("Response does not have the appropriate Content-Type field for this method");
            return JsonParser.parseString(new String(buffer, contentType.charset != null ? contentType.charset : StandardCharsets.UTF_8));
        }

        /**
         * Gets this response's body as plain text
         * @return A String representation of this response's body
         * @throws ResponseException If response has no body or incorrect Content-Type
         */
        public String text() throws ResponseException
        {
            if (buffer == null)
                throw new ResponseException("Response has no body");
            if (!contentType.mediaType.startsWith("text/"))
                throw new ResponseException("Response does not have the appropriate Content-Type field for this method");
            return new String(buffer, contentType.charset != null ? contentType.charset : StandardCharsets.UTF_8);
        }

        /**
         * Gets this response's body as an image
         * @return An Image representation of this response's body
         * @throws ResponseException If response has no body, an incorrect Content-Type, or failed to retrieve image data
         */
        public Image image() throws ResponseException
        {
            if (buffer == null)
                throw new ResponseException("Response has no body");
            if (!contentType.mediaType.startsWith("image/"))
                throw new ResponseException("Response does not have the appropriate Content-Type field for this method");
            try
            {
                return ImageIO.read(new MemoryCacheImageInputStream(new ByteArrayInputStream(buffer)));
            }
            catch (IOException ex)
            {
                throw new ResponseException("An unknown error occurred while attempting to gather image data of this response");
            }
        }

        /**
         * Gets this response's body as an audio stream
         * @return An AudioInputStream representation of this response's body
         * @throws ResponseException If response has no body or incorrect Content-Type
         */
        public AudioInputStream audio() throws ResponseException
        {
            if (buffer == null)
                throw new ResponseException("Response has no body");
            if (!contentType.mediaType.startsWith("audio/"))
                throw new ResponseException("Response does not have the appropriate Content-Type field for this method");
            return new AudioInputStream(new ByteArrayInputStream(buffer), new AudioFormat(44100, 16, 2, true, false), buffer.length);
        }

        /**
         * Gets this response's body as a byte buffer.
         * @return A ByteBuffer representation of this response's body
         */
        public ByteBuffer buffer()
        {
            return ByteBuffer.wrap(buffer);
        }

        /**
         * Gets the raw bytes of this response's body.
         * @return The byte array of this response's body
         */
        public byte[] raw()
        {
            return buffer;
        }

        /**
         * @return Whether the response code is successful or not (200)
         */
        public boolean successful()
        {
            return code == 200;
        }

        /**
         * @return The response code of this response
         */
        public int getCode()
        {
            return code;
        }

        /**
         * @return The response message of this response
         */
        public String getMessage()
        {
            return message;
        }

        /**
         * @return The Content-Type of this response
         */
        public ContentType getContentType()
        {
            return contentType;
        }

        public String toString()
        {
            return new String(buffer, contentType.charset != null ? contentType.charset : StandardCharsets.UTF_8);
        }
    }

    public static class Parameters implements Iterable<Map.Entry<String, Object>>
    {
        private Map<String, Object> map;

        public Parameters()
        {
            this.map = new HashMap<>();
        }

        /**
         * Appends a parameter with a key corresponding to its value
         * @param key The identifier of the parameter
         * @param value The underlying value of the parameter
         * @return This object for chaining of method calls
         */
        public Parameters append(String key, Object value)
        {
            this.map.put(key, value);
            return this;
        }

        /**
         * Appends a parameter with only a key
         * @param key The identifier of the parameter
         * @return This object for chaining of method calls
         */
        public Parameters append(String key)
        {
            this.map.put(key, true);
            return this;
        }

        public Parameters remove(String key)
        {
            this.map.remove(key);
            return this;
        }

        public Object get(String key)
        {
            return this.map.get(key);
        }

        public int count()
        {
            return this.map.size();
        }

        @Override
        public String toString()
        {
            if (this.map.size() == 0)
                return "";
            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, Object> entry : this.map.entrySet())
            {
                if (entry.getValue().equals(true))
                    joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                else
                    joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            }
            return joiner.toString();
        }

        @Override
        public Iterator<Map.Entry<String, Object>> iterator()
        {
            return this.map.entrySet().iterator();
        }
    }

    public static class Headers implements Iterable<Map.Entry<String, Object>>
    {
        private Map<String, Object> map;

        public Headers()
        {
            this.map = new HashMap<>();
        }

        /**
         * Appends a parameter with a key corresponding to its value
         * @param key The identifier of the parameter
         * @param value The underlying value of the parameter
         * @return This object for chaining of method calls
         */
        public Headers append(String key, Object value)
        {
            this.map.put(key, value);
            return this;
        }

        /**
         * Appends a parameter with only a key
         * @param key The identifier of the parameter
         * @return This object for chaining of method calls
         */
        public Headers append(String key)
        {
            this.map.put(key, true);
            return this;
        }

        public Headers remove(String key)
        {
            this.map.remove(key);
            return this;
        }

        public Object get(String key)
        {
            return this.map.get(key);
        }

        public int count()
        {
            return this.map.size();
        }

        @Override
        public String toString()
        {
            if (this.map.size() == 0)
                return "";
            StringJoiner joiner = new StringJoiner(", ");
            for (Map.Entry<String, Object> entry : this.map.entrySet())
            {
                joiner.add(entry.getKey() + ": " + entry.getValue().toString());
            }
            return joiner.toString();
        }

        @Override
        public Iterator<Map.Entry<String, Object>> iterator()
        {
            return this.map.entrySet().iterator();
        }
    }

    private static class ContentType
    {
        private String mediaType;
        private Charset charset;
        private String boundary;

        private ContentType(String s)
        {
            String[] parts = s.split("; ");
            this.mediaType = "text/plain";
            this.charset = null;
            this.boundary = null;
            for (String part : parts)
            {
                String[] mapped = part.split("=");
                switch (mapped[0])
                {
                    case "charset":
                    {
                        this.charset = Charset.forName(mapped[1]);
                        break;
                    }
                    case "boundary":
                    {
                        this.boundary = mapped[1];
                        break;
                    }
                    default:
                    {
                        this.mediaType = mapped[0];
                        break;
                    }
                }
            }
        }

        @Override
        public String toString()
        {
            return "ContentType{" +
                    "mediaType=" + mediaType +
                    ", charset=" + charset +
                    ", boundary=" + boundary +
                    '}';
        }
    }

    public static class ResponseException extends RuntimeException
    {
        public ResponseException(String message)
        {
            super(message);
        }
    }
}