import { Entity, PrimaryGeneratedColumn, Column, ManyToMany, OneToMany, PrimaryColumn } from "typeorm"
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

    @ManyToMany(() => User, user => user.categorias)
    usuarios: User[]

    @OneToMany(() => Transacao, transacao => transacao.categoria)
    transacoes: Transacao[]
}