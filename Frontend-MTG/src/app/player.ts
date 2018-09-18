export class Player {
    name: string;
    email: string;
    password: string;
    life: number;
    poison: number;
    experience: number;
    commanderDamage: Map<string, Map<string, number>>;
    constructor(name: string, email: string, password: string, life: number) { // TODO Might have to initialize the commanderDamage
        this.name = name;
        this.email = email;
        this.password = password;
        this.life = life;
        this.poison = 0;
        this.experience = 0;
    }
}
