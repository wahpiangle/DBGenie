export interface ChatMessage {
    message: string;
    fromServer: boolean;
    pending?: boolean;
}

export interface Property {
    id: string;
    name: string;
    description: string;
    createdAt: Date;
    updatedAt: Date;
    userId: string;
    imageUrl: string[];
    createdBy: User;
    bookings: Booking[];
    MaintenanceRequest: MaintenanceRequest[];
}

export interface User {
    id: string;
    email: string;
    name: string;
    role: string;
    properties: Property[];
    bookings: Booking[];
    MaintenanceRequest: MaintenanceRequest[];
}

export interface Booking {
    id: string;
    checkIn: Date;
    checkOut: Date;
    propertyId: string;
    property: Property;
    userId: string;
    user: User;
}

export interface MaintenanceRequest {
    id: string;
    title: string;
    description: string;
    propertyId: string;
    property: Property;
    userId: string;
    user: User;
}