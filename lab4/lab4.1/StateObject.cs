using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace lab4._1
{
    class StateObject
    {
        public Socket socket = null;

        public const int BUFFER_SIZE = 512;
  
        public byte[] buffer = new byte[BUFFER_SIZE];

        // received data  
        public StringBuilder responseContent = new StringBuilder();

        public int id;

        public string hostname;

        public string endpoint;

        public IPEndPoint remoteEndpoint;

        // mutex for "connect" operation
        public ManualResetEvent connectDone = new ManualResetEvent(false);

        // mutex for "send" operation
        public ManualResetEvent sendDone = new ManualResetEvent(false);

        // mutex for "receive" operation
        public ManualResetEvent receiveDone = new ManualResetEvent(false);
    }
}
