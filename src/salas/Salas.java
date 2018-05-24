package salas;

import listaDesordenada.*;
import sala.*;
import usuario.*;
import java.net.*;

public class Salas
{
    protected ListaDesordenada<Sala> salas;

    public Salas()
    {
        this.salas = new ListaDesordenada<Sala>();
    }

    public synchronized void novaSala (String nome) throws Exception
    {
        if(nome == null)
            throw new Exception("Nome nulo!");

        Sala sl = new Sala(nome);
        salas.inserirNoInicio(sl);
    }

    public synchronized int qtd()
    {
    	return this.salas.getQtd();
    }

    public synchronized void novoUsuario (String nomSal, String nicUsr) throws Exception
    {
        if(nomSal==null || nicUsr == null)
            throw new Exception("Valores nulos!");

        Usuario us = new Usuario (this.getNomesSalas(), new Socket(nicUsr, 12345));

        this.getSala(nomSal).usuarioEntrar(us);
    }

    public synchronized Sala getSala (String nome) throws Exception
    {
        if(nome == null)
            throw new Exception("Nome nulo!");

        this.salas.mudarAtualLista(0);
        for(int i=0; i<this.salas.getQtd(); i++)
        {
            if(this.salas.getAtualLista().getNome().equals(nome))
                return this.salas.getAtualLista();
            this.salas.andarAtualLista();
        }

        throw new Exception("Essa sala nao existe!");
    }

    public synchronized String[] getNomesSalas () throws Exception
    {
        String[] ret = new String[this.salas.getQtd()];

        this.salas.mudarAtualLista(0);
        for(int i=0; i<this.salas.getQtd(); i++)
        {
            ret[i] = this.salas.getAtualLista().getNome();
            this.salas.andarAtualLista();
        }

        return ret;
    }

    public synchronized Usuario[] getUsuariosSala (String nomSal) throws Exception
    {
        return this.getSala(nomSal).getUsuarios();
    }

    public synchronized Usuario getUsuario (String nomSal, String nicUsr) throws Exception
    {
    	try {
        return this.getSala(nomSal).getUsuario(nicUsr);
    	}catch(Exception e)
    	{
    		throw new Exception("Erro ao identificar o usuario");
    	}
    }

    public synchronized void tiraUsuario (String nomSal, String nicUsr) throws Exception
    {
        this.getSala(nomSal).usuarioSair(nicUsr);   
    }

    public synchronized void fechaSala (String nome) throws Exception {}

    // metodos obrigatorios
    public String toString()
    {
        String ret = "{Salas: " + this.salas + "}";
        return ret;
    }
    
    public synchronized String[] getNick(Sala sala) throws Exception
    {
    	Usuario[] uss = getUsuariosSala(sala.getNome());
    	String[] nicks = new String[uss.length];
    	for (int i=0; i<uss.length; i++)
    	{
    		nicks[i] = uss[i].getNick();
    	}
    	return nicks;
    }

    public synchronized int hashCode()
    {
        int ret = 7;
        ret = ret*7 + this.salas.hashCode();
        return ret;
    }

    public synchronized boolean equals(Object obj)
    {
        if(obj==null)
            return false;
        if(this==obj)
            return true;

        if(obj.getClass() != this.getClass())
            return false;

        Salas sls = (Salas)obj;

        if(this.salas.equals(sls.salas))
            return true;

        return false;
    }
}
