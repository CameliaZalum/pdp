using System;
using System.Collections.Generic;

namespace lab4._1
{
    class Program
    {
        private static readonly List<string> HOSTS = new List<string> {
            // - gzip form 
            "en.wikipedia.org/wiki/Gzip",
            // - empty body 
            "twitter.com/?lang=ro",
            // - plain text
            "google.com",
        };
        static void Main(string[] args)
        {
            AsyncTasks.setHosts(HOSTS);
            AsyncTasks.start();

            TaskMec.setHosts(HOSTS);
            TaskMec.start();

            DirectCallback.setHosts(HOSTS);
            DirectCallback.start();
        }
    }
}
