import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from "typeorm"
import { User } from "./User"
import { Banco } from "./Banco"

@Entity()
export class UsuarioBanco {
    @PrimaryGeneratedColumn()
    usuarioBancoID: number

    @Column()
    UserId: number

    @Column()
    BancoId: number

    @Column("decimal", { precision: 10, scale: 2 })
    valor: number

    @ManyToOne(() => User, user => user.usuarioBancos)
    @JoinColumn({ name: "UserId" })
    user: User

    @ManyToOne(() => Banco, banco => banco.usuarioBancos)
    @JoinColumn({ name: "BancoId" })
    banco: Banco
}