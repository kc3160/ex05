export interface User {
    type: string;
    name: string;
    email: string;
    password: string;
}

export class Helper implements User {
    type: string = "helper";
    name: string;
    email: string;
    password: string;
    basketId?: number;

    constructor(name: string, email: string, password: string) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

export class Admin implements User {
    type: string = "admin";
    name: string;
    email: string;
    password: string;

    constructor(name: string, email: string, password: string) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}