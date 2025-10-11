import type { TableData } from "../../interfaces/TableData";
import { useState } from "react";
import TableHeadControl from "../TableHeadControl";
import type { LabWork } from "../../interfaces/Entities/LabWork";

function LabWorkTable(params : {tableData: TableData<LabWork>}) 
{
    const [chosenRow, setChosenRow] = useState(-1)
    return (
        <table cellPadding="8" cellSpacing="0" className="main-table">
        <thead>
            <tr>
                <th className="main-table-head-labels">ID</th>
                <th className="main-table-head-labels">Имя</th>
                <th className="main-table-head-labels">ID координаты</th>
                <th className="main-table-head-labels">Дата создания</th>
                <th className="main-table-head-labels">Описание</th>
                <th className="main-table-head-labels">Сложность</th>
                <th className="main-table-head-labels">ID Дисциплины</th>
                <th className="main-table-head-labels">Минимальная оценка</th>
                <th className="main-table-head-labels">Максимальная оценка</th>
                <th className="main-table-head-labels">ID Автора</th>
            </tr>
            <tr>
                <TableHeadControl element="id" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="name" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="coordinateId" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="creationDate" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="description" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="difficulty" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="disciplineId" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="minimalPoint" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="maximalPoint" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="authorId" enabled={true} tableData={params.tableData}/>
            </tr>
        </thead>
        <tbody>
            {params.tableData.data.result.map(item => (
                <tr key={item.id} className={`main-table-data-row ${item.id == chosenRow ? "main-table-data-row-selected" : ""}`} 
                onClick={()=>setChosenRow(item.id)}>
                    <td className="main-table-elem">{item.id}</td>
                    <td className="main-table-elem">{item.name}</td>
                    <td className="main-table-elem">{item.coordinate?.id}</td>
                    <td className="main-table-elem">{item.creationDate}</td>
                    <td className="main-table-elem">{item.description}</td>
                    <td className="main-table-elem">{item.difficulty?.val}</td>
                    <td className="main-table-elem">{item.discipline?.id}</td>
                    <td className="main-table-elem">{item.minimalPoint}</td>
                    <td className="main-table-elem">{item.maximalPoint}</td>
                    <td className="main-table-elem">{item.author?.id}</td>
                </tr>
            ))}
        </tbody>
        </table>
    )
}

export default LabWorkTable