import type { Request, Response } from "express";
import appGraph from "../lib/graph";
import { runMultipleQueries } from "../lib/pgdb";
import format from "pg-format";
import checkpointer from "../lib/checkpointer";

export class ChatController {
    public static async createChat(req: Request, res: Response) {
        const { inputText } = req.body;
        const { user } = req.session;
        // await ChatController.resetThread(user.id);
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

    public static async clearChatHistory(req: Request, res: Response) {
        const { user } = req.session;
        const threadId = user.id;
        try {
            await this.resetThread(threadId);
            res.status(200).json({ message: "Chat history cleared" });
        } catch (error) {
            console.error("Error clearing chat history:", error);
            res.status(500).json({ message: "Error clearing chat history" });
        }
    }

    public static async getChatHistory(req: Request, res: Response) {
        const { user } = req.session;
        const threadId = user.id;
        try {
            const data = await checkpointer.get({
                configurable: {
                    thread_id: threadId,
                }
            })
            res.status(200).json(data?.channel_values);
        } catch (error) {
            console.error("Error getting chat history:", error);
            res.status(500).json({ message: "Error getting chat history" });
        }
    }

    private static async resetThread(threadId: string) {
        await runMultipleQueries(
            [
                format("DELETE FROM checkpoints WHERE thread_id = %L", threadId),
                format("DELETE FROM checkpoint_blobs WHERE thread_id = %L", threadId),
                format("DELETE FROM checkpoint_writes WHERE thread_id = %L", threadId),
            ]
        );
    }
}
