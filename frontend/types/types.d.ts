export interface ChatMessage {
    message: string;
    fromServer: boolean;
    pending?: boolean;
}

export interface Property {
    id: string;
    name: string;
    description: string;
    created_at: Date;
    updated_at: Date;
    userId: string;
    image_url: string[];
    created_by: User;
    bookings: Booking[];
    MaintenanceRequest: MaintenanceRequest[];
}

export interface User {
    id: string;
    email: string;
    name: string;
    role: Role;
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