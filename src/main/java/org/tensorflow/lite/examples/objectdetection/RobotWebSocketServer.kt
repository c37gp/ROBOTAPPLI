package org.tensorflow.lite.examples.objectdetection

import fi.iki.elonen.NanoHTTPD
import java.io.ByteArrayOutputStream
import java.util.concurrent.CopyOnWriteArrayList

class RobotWebSocketServer(
    private val robotController: RobotController
) : NanoHTTPD(8080) {

    private val clients = CopyOnWriteArrayList<IHTTPSession>()

    override fun serve(session: IHTTPSession): Response {

        val uri = session.uri

        return when {

            // -------------------------
            // UI iPhone (HTML)
            // -------------------------
            uri == "/" -> {
                newFixedLengthResponse(
                    Response.Status.OK,
                    "text/html",
                    IOS_UI_HTML
                )
            }

            // -------------------------
            // commandes iPhone → robot
            // -------------------------
            uri.startsWith("/cmd/") -> {

                val cmd = uri.removePrefix("/cmd/")
                robotController.onWebCommand(cmd)

                newFixedLengthResponse("OK")
            }

            // -------------------------
            // streaming simple MJPEG fallback
            // -------------------------
            uri == "/frame" -> {

                val frame = robotController.getLatestFrame()

                newFixedLengthResponse(
                    Response.Status.OK,
                    "image/jpeg",
                    frame?.inputStream(),
                    frame?.size ?: 0L
                )
            }

            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404")
        }
    }

    companion object {

        val IOS_UI_HTML = """
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Robot Control</title>

<style>
body {
    margin:0;
    background:#0f0f0f;
    color:white;
    font-family:Arial;
}

#video {
    width:100%;
    height:40vh;
    background:black;
}

.grid {
    display:grid;
    grid-template-columns:1fr 1fr 1fr;
    gap:10px;
    padding:20px;
}

button {
    height:80px;
    border-radius:50%;
    border:none;
    font-size:28px;
    background:#333;
    color:white;
}

#stop {
    background:red;
}
</style>
</head>

<body>

<img id="video" src="/frame">

<div class="grid">
<button onclick="send('forward')">▲</button>
<button onclick="send('left')">◀</button>
<button id="stop" onclick="send('stop')">⏹</button>
<button onclick="send('right')">▶</button>
<button onclick="send('backward')">▼</button>
</div>

<script>
function send(cmd){
    fetch("/cmd/" + cmd);
}
setInterval(()=>{
    document.getElementById("video").src="/frame?t="+Date.now();
}, 150);
</script>

</body>
</html>
"""
    }
}
