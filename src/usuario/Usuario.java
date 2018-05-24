package usuario;

import java.io.*;
import java.net.*;

public class Usuario
{
    protected String         nomeSala;
    protected String         nick;
    protected Socket         conexao;
    protected BufferedReader receptor;
    protected PrintWriter    transmissor;

    // construtor de usuario instancia BufferedReader e PrintWriter, 
    // interage atraves deles com o usuario para enviar a lista de
    // salas e obter a sala onde o usuario quer entrar, bem como o
    // seu nick, inicializando this.sala e this.nick
    public Usuario(String[] nomSls, Socket conexao) throws Exception
    {
        if(conexao == null || nomSls == null)
            throw new Exception("Dados do usuário nulos!");

        this.conexao = conexao;
        this.receptor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        this.transmissor = new PrintWriter(conexao.getOutputStream());

        //this.transmissor.println("Escolha uma das salas (digite o numero da sala): ");
        //for(int i = 0; i < nomSls.length; i++)
        	//this.transmissor.println(i + " - " + nomSls[i]);
        
        //try
        //{
            //int sl = Integer.parseInt(this.receptor.readLine());
            //this.nomeSala = nomSls[sl];
        //}catch(Exception e)
        //{
            //throw new Exception("Numero da sala invalido!");
        //}

        //this.transmissor.println("Escolha seu nick: ");
        //String n = this.receptor.readLine();
        //if(n.contains("/&*_/") || n.equals("TODOS"))
            //throw new Exception("Nome invalido!");
        //this.nick = n;
    }
    
    public Usuario(String nick, String nomSl, Socket conexao) throws Exception
    {
        if(conexao == null || nomSl == null)
            throw new Exception("Dados do usuário nulos!");

        this.conexao = conexao;
        this.receptor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        this.transmissor = new PrintWriter(conexao.getOutputStream());

        this.nomeSala = nomSl;

        this.nick = nick;
    }

    //setters para nomeSala e nick
    public void setNick(String n) throws Exception
    {
    	if(n == null)
    		throw new Exception("Nick eh nulo");
    	
        this.nick = n;
    }

    public void setSala(String s) throws Exception
    {
    	if(s == null)
    		throw new Exception("Sala escolhida eh nula");
    	
        this.nomeSala = s;
    }
    
    // getters para nomeSala e nick
    public String getNick()
    {
        return this.nick;
    }

    public String getSala()
    {
        return this.nomeSala;
    }

    // metodos para desconectar

    // recebe do usuario, usando this.receptor
    public String recebe() throws Exception
    {
    	return this.receptor.readLine();
    }

    // envia para o usuario, usando this.transmissor
    public void envia(String s) throws Exception
    {
        if(s==null)
            throw new Exception("String nula!");

        this.transmissor.println(s);
        this.transmissor.flush();
    }

    // metodos obrigatorios
    public String toString()
    {
        String ret = "{Nick: " + this.nick + ", Sala: " + this.nomeSala + "}";
        return ret;
    }

    public int hashCode()
    {
        int ret = 7;
        ret = ret*7 + this.nomeSala.hashCode();
        ret = ret*7 + this.nick.hashCode();
        ret = ret*7 + this.conexao.hashCode();
        ret = ret*7 + this.receptor.hashCode();
        ret = ret*7 + this.transmissor.hashCode();

        return ret;
    }

    public boolean equals(Object obj)
    {
        if(obj==null)
            return false;
        if(this==obj)
            return true;

        if(obj.getClass() != this.getClass())
            return false;

        Usuario us = (Usuario)obj;

        if(this.nomeSala.equals(us.nomeSala) && this.nick.equals(us.nick) && this.conexao.equals(us.conexao) 
            && this.transmissor.equals(us.transmissor) && this.receptor.equals(us.receptor))
            return true;
        return false;
    }
}
