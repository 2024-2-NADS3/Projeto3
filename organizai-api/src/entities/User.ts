import { Entity, PrimaryGeneratedColumn, Column, OneToOne, ManyToMany, JoinTable, Unique, OneToMany } from "typeorm"
import { Quiz } from "./Quiz"
import { Categoria } from "./Categoria"
import { Transacao } from "./Transacao"
import { UsuarioBanco } from "./UsuarioBanco"
import { IsNotEmpty, IsEmail } from "class-validator"

@Entity()
@Unique(["email"])
export class User {
    @PrimaryGeneratedColumn()
    UserId: number

    @Column()
    @IsNotEmpty({ message: "O nome não pode estar vazio" })
    nome: string

    @Column()
    @IsNotEmpty({ message: "O sobrenome não pode estar vazio" })
    sobrenome: string

    @Column({ nullable: true })
    cpf: string

    @Column()
    @IsNotEmpty({ message: "O email não pode estar vazio" })
    @IsEmail({}, { message: "Email inválido" })
    email: string

    @Column()
    @IsNotEmpty({ message: "O telefone não pode estar vazio" })
    telefone: string

    @Column()
    @IsNotEmpty({ message: "A senha não pode estar vazia" })
    senha: string

    @OneToOne(() => Quiz, quiz => quiz.user)
    quiz: Quiz

    @ManyToMany(() => Categoria)
    @JoinTable({ name: "UsuarioCategoria" })
    categorias: Categoria[]

    @OneToMany(() => Transacao, transacao => transacao.usuario)
    transacoes: Transacao[]

    @OneToMany(() => UsuarioBanco, usuarioBanco => usuarioBanco.user)
    usuarioBancos: UsuarioBanco[]
}