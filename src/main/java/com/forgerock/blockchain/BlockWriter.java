package com.forgerock.blockchain;

//rj? import com.google.gson.GsonBuilder;

import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BlockWriter {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static String getResult (String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        String date = sdf.format(new Date());

        blockchain.add(new Block("previous block", "0"));
        blockchain.add(new Block(msg + date, blockchain.get(blockchain.size()-1).hash));
//      return blockchain.toString();

       String blockchainjson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        return blockchainjson;
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

    public static ArrayList getResults (String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        String date = sdf.format(new Date());

        blockchain.add(new Block("block ...", "... "));
        blockchain.add(new Block(msg + date, blockchain.get(blockchain.size() - 1).hash));
        return blockchain;
    }

}
