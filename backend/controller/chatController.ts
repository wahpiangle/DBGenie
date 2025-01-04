import type { Request, Response } from "express";
import { prisma } from "../prisma";
import appGraph from "../lib/graph";

export class ChatController {
    public static async createChat(req: Request, res: Response) {
        const { inputText } = req.body;
        const { user } = req.session;
        const response = await appGraph.invoke({
            user: user,
            question: inputText,
        },
            {
                configurable: {
                    thread_id: user.id
                }
            },
        )

        res.status(200).json({ response: response.error ?? response.result });
    }
}
