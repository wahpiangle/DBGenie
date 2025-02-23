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
    check_in: string;
    check_out: string;
    propertyId: string;
    property: Property;
    userId: string;
    user: User;
    remarks: string?;
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

export interface databaseInfo {
    tables: databaseTable[]
    relationships: databaseRelationship[]
}

export interface databaseTable {
    table_name: string,
    columns: databaseColumn[]
}

export interface databaseColumn {
    name: string,
    type: string
}

export interface databaseRelationship {
    from: string,
    fromColumn: string,
    to: string,
    toColumn: string
}