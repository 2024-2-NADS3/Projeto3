import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from "typeorm"
import { UsuarioBanco } from "./UsuarioBanco"

@Entity()
export class Banco {
    @PrimaryGeneratedColumn()
    BancoId: number

    @Column()
    nomeBanco: string

    @OneToMany(() => UsuarioBanco, usuarioBanco => usuarioBanco.banco)
    usuarioBancos: UsuarioBanco[]
}