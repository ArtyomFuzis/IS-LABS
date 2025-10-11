import type { TableData } from "../../interfaces/TableData";
import { useState } from "react";
import TableHeadControl from "../TableHeadControl";
import type { Person } from "../../interfaces/Entities/Person";

function PersonTable(params : {tableData: TableData<Person>}) 
{
    const [chosenRow, setChosenRow] = useState(-1)
    return (
        <table cellPadding="8" cellSpacing="0" className="main-table">
        <thead>
            <tr>
                <th className="main-table-head-labels">ID</th>
                <th className="main-table-head-labels">Имя</th>
                <th className="main-table-head-labels">Цвет глаз</th>
                <th className="main-table-head-labels">Цвет волос</th>
                <th className="main-table-head-labels">ID локации</th>
                <th className="main-table-head-labels">Паспорт</th>
                <th className="main-table-head-labels">Национальность</th>
            </tr>
            <tr>
                <TableHeadControl element="id" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="name" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="eyeColor" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="hairColor" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="locationId" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="passport" enabled={true} tableData={params.tableData}/>
                <TableHeadControl element="nationality" enabled={true} tableData={params.tableData}/>
            </tr>
        </thead>
        <tbody>
            {params.tableData.data.result.map(item => (
                <tr key={item.id} className={`main-table-data-row ${item.id == chosenRow ? "main-table-data-row-selected" : ""}`} 
                onClick={()=>setChosenRow(item.id)}>
                    <td className="main-table-elem">{item.id}</td>
                    <td className="main-table-elem">{item.name}</td>
                    <td className="main-table-elem">{item.eyeColor?.val}</td>
                    <td className="main-table-elem">{item.hairColor?.val}</td>
                    <td className="main-table-elem">{item.location?.id}</td>
                    <td className="main-table-elem">{item.passportId}</td>
                    <td className="main-table-elem">{item.nationality?.val}</td>
                </tr>
            ))}
        </tbody>
        </table>
    )
}

export default PersonTable