import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, PrimaryColumn } from "typeorm"
import { User } from "./User"
import { Transacao } from "./Transacao"

@Entity()
export class Categoria {
    @PrimaryColumn()
    CategoriaId: number

    @Column()
    nomeCat: string

    @Column()
    tipo: number

    @Column("decimal", { precision: 10, scale: 2 })
    total: number

    @ManyToOne(() => User, user => user.categorias)
    usuario: User

    @OneToMany(() => Transacao, transacao => transacao.categoria)
    transacoes: Transacao[]
}