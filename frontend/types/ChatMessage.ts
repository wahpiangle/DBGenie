export interface ChatMessage {
    message: string;
    fromServer: boolean;
    pending?: boolean;
}