package com.github.tanukkii007.nettyinaction.chapter1

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.net.{ServerSocket, Socket}


trait BlockingIoExample {

  def serve(portNumber: Int): Unit = {
    val serverSocket = new ServerSocket(portNumber)
    val clientSocket: Socket = serverSocket.accept()
    val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
    val out = new PrintWriter(clientSocket.getOutputStream, true)
    var break = false
    while (!break) {
      val request = in.readLine()
      if (request != null) {
        if ("Done" == request) {
          break = true
        } else {
          val response = processRequest(request)
          out.println(response)
        }
      }


    }
  }

  protected def processRequest(request: String): String

}
