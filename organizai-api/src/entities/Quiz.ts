import { Entity, PrimaryGeneratedColumn, Column, OneToOne, JoinColumn } from "typeorm"
import { User } from "./User"

@Entity()
export class Quiz {
    @PrimaryGeneratedColumn()
    QuizId: number

    @Column()
    UserId: number

    @Column()
    isAnswered: boolean 

    @Column({ nullable: true })
    isCadUni: boolean

    @Column("decimal", { precision: 10, scale: 2, nullable: true })
    rendaMensal: number

    @Column({ nullable: true })
    qtnPorFamilia: number

    @Column({ nullable: true })
    isOlder: boolean

    @Column({ nullable: true })
    isRural: boolean

    @Column("date")
    dataCriacao: Date

    @OneToOne(() => User, user => user.quiz)
    @JoinColumn({ name: "UserId" })
    user: User
}