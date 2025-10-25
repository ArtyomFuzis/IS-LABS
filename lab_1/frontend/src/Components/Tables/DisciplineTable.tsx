import type { TableData } from "../../interfaces/TableData";
import { useState } from "react";
import TableHeadControl from "../TableHeadControl";
import type { Discipline } from "../../interfaces/Entities/Discipline";

function DisciplineTable(params : {tableData: TableData<Discipline>, chosenRow: number, setChosenRow: React.Dispatch<number>}) 
{
    return (
        <table cellPadding="8" cellSpacing="0" className="main-table">
        <thead>
            <tr>
                <th className="main-table-head-labels">ID</th>
                <th className="main-table-head-labels">Имя</th>
                <th className="main-table-head-labels">Кол-во лабораторных</th>
            </tr>
            <tr>
                <TableHeadControl element="id" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="name" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="labsCount" enabled={true} tableData={params.tableData}/>
            </tr>
        </thead>
        <tbody>
            {params.tableData.data.result.map(item => (
                <tr key={item.id} className={`main-table-data-row ${item.id == params.chosenRow ? "main-table-data-row-selected" : ""}`} 
                onClick={()=>params.setChosenRow(item.id)}>
                    <td className="main-table-elem">{item.id}</td>
                    <td className="main-table-elem">{item.name}</td>
                    <td className="main-table-elem">{item.labsCount}</td>
                </tr>
            ))}
        </tbody>
        </table>
    )
}

export default DisciplineTable