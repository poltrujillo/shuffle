using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.Data.SqlTypes;



public class Cliente
{
    public string Name { get; set; }
    public TcpClient Conexion { get; set; }

    public bool ready { get; set; }
    public Cliente (string name, TcpClient conexion, bool r)
    {
        Name = name;
        Conexion = conexion;
        ready = r;
    }
}

