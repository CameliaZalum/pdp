using lab4._1;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;


namespace lab4._1
{
    class AsyncTasks
    {
        private static List<string> hosts;

        public static void setHosts(List<string> hosts)
        {
            AsyncTasks.hosts = hosts;
        }
        public static void start()
        {
            var tasks = new List<Task>();
            for (var i = 0; i < AsyncTasks.hosts.Count; i++)
            {
                tasks.Add(Task.Factory.StartNew(doStart, i));
            }

            Task.WaitAll(tasks.ToArray());
        }
        private static void doStart(object idObject)
        {
            var id = (int)idObject;

            StartClient(AsyncTasks.hosts[id], id);
        }

        private static async void StartClient(string host, int id)
        {
             
            var ipHostInfo = Dns.GetHostEntry(host.Split('/')[0]);
            var ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint remoteEndpoint = new IPEndPoint(ipAddress, Utils.HTTP_PORT);

            
            var client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            // wrapper 
            var state = new StateObject
            {
                socket = client,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/")) : "/",
                remoteEndpoint = remoteEndpoint,
                id = id
            };

            await ConnectWrapper(state);
            
            await SendWrapper(state, Utils.getRequestString(state.hostname, state.endpoint));

            await ReceiveWrapper(state);

            Console.WriteLine(
                "{0} --> received : expected {1} chars in body, got {2} ",
                id, Utils.getContentLength(state.responseContent.ToString()), state.responseContent.Length);

            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        private static async Task ConnectWrapper(StateObject state)
        {
            state.socket.BeginConnect(state.remoteEndpoint, ConnectCallback, state);

            await Task.FromResult<object>(state.connectDone.WaitOne());
        }

        private static void ConnectCallback(IAsyncResult ar)
        {
            var state = (StateObject)ar.AsyncState;
            var clientSocket = state.socket;
            var clientId = state.id;
            var hostname = state.hostname;

            clientSocket.EndConnect(ar);
            Console.WriteLine("{0} --> Socket connected to {1} ({2})", clientId, hostname, clientSocket.RemoteEndPoint);
            state.connectDone.Set();
        }

        private static async Task SendWrapper(StateObject state, string data)
        {
            var byteData = Encoding.ASCII.GetBytes(data);
            state.socket.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);
            await Task.FromResult<object>(state.sendDone.WaitOne());
        }

        private static void SendCallback(IAsyncResult ar)
        {
            var state = (StateObject)ar.AsyncState;
            var clientSocket = state.socket;
            var clientId = state.id;
            var bytesSent = clientSocket.EndSend(ar);
            Console.WriteLine("{0} --> Sent {1} bytes to server.", clientId, bytesSent);

           
            state.sendDone.Set();
        }

        private static async Task ReceiveWrapper(StateObject state)
        {
            state.socket.BeginReceive(state.buffer, 0, StateObject.BUFFER_SIZE, 0, ReceiveCallback, state);
            await Task.FromResult<object>(state.receiveDone.WaitOne());
        }

        private static void ReceiveCallback(IAsyncResult ar)
        {
            var state = (StateObject)ar.AsyncState;
            var clientSocket = state.socket;

            try
            {
                var bytesRead = clientSocket.EndReceive(ar);

                state.responseContent.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

                if (!Utils.responseHeaderFullyObtained(state.responseContent.ToString()))
                {
                    clientSocket.BeginReceive(state.buffer, 0, StateObject.BUFFER_SIZE, 0, ReceiveCallback, state);
                }
                else
                {
                    // get the body
                    var responseBody = Utils.getResponseBody(state.responseContent.ToString());

                    if (responseBody.Length < Utils.getContentLength(state.responseContent.ToString()))
                    {
                        // if it isn't, than more data is to be retrieve
                        clientSocket.BeginReceive(state.buffer, 0, StateObject.BUFFER_SIZE, 0, ReceiveCallback, state);
                    }
                    else
                    {
                        // all bytes received  
                        state.receiveDone.Set();
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}
