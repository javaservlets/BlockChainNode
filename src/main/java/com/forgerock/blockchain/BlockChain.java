/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 ForgeRock AS.
 */


package com.forgerock.blockchain;

import com.google.inject.assistedinject.Assisted;
import com.sun.identity.shared.debug.Debug;
import org.forgerock.openam.auth.node.api.*;
import org.forgerock.openam.core.CoreWrapper;

import javax.inject.Inject;
import javax.security.auth.callback.TextOutputCallback;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import static org.forgerock.openam.auth.node.api.Action.send;

/**
 * A node that starts up a locally run block chain and logs FRDP data to it
 */
@Node.Metadata(outcomeProvider = SingleOutcomeNode.OutcomeProvider.class, configClass = BlockChain.Config.class)

public class BlockChain extends SingleOutcomeNode {
    private final static String DEBUG_FILE = "BlockNodes";
    protected Debug debug = Debug.getInstance(DEBUG_FILE);
    private List list;
    private final Config config;
    private final CoreWrapper coreWrapper;

    @Override
    public Action process(TreeContext context) throws NodeProcessException {
        try {
            ArrayList result;
            BlockWriter block = new BlockWriter();
            result = block.getResults("auditStamp: ");
            //log(result.toString());

            String blockchainjson = new GsonBuilder().setPrettyPrinting().create().toJson(result);
            log(blockchainjson);

            return send(new TextOutputCallback(0, "chain stamped: " + blockchainjson)).build();

        } catch (Exception e) {
            log ("      e throw: " + e.toString());
        }
        return goToNext().build();
    }


    public void log(String str) {
        debug.error("\r\n   blockchain node::" + str);
//        System.out.println("+++    blockchain node:" + str);
    }


    public interface Config {
    }


    @Inject
    public BlockChain(@Assisted Config config, CoreWrapper coreWrapper) throws NodeProcessException {
        this.config = config;
        this.coreWrapper = coreWrapper;
    }

}