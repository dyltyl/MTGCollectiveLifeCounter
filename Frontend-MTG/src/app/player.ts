export class Player {
    name: string;
    email: string;
    password: string;
    life: number;
    poison: number;
    experience: number;
    commanderDamage: Map<string, Map<string, number>>;
}
