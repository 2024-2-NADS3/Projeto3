import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from "typeorm"
import { User } from "./User"
import { Categoria } from "./Categoria"

@Entity()
export class Transacao {
    @PrimaryGeneratedColumn()
    TransacaoId: number

    @Column()
    UsuarioId: number

    @Column()
    CategoriaId: number

    @Column()
    isReceita: boolean

    @Column("decimal", { precision: 10, scale: 2 })
    valor: number

    @Column()
    descricao: string

    @Column("date")
    data: Date

    @ManyToOne(() => User, user => user.transacoes)
    @JoinColumn({ name: "UsuarioId" })
    usuario: User

    @ManyToOne(() => Categoria, categoria => categoria.transacoes)
    @JoinColumn({ name: "CategoriaId" })
    categoria: Categoria
}