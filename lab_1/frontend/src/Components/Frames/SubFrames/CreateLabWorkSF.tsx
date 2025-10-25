import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsNotNull, processIsValidFloat, processObjectChosen, processStringMaxLength, processStringMinLength } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate } from "../../../Utils/baseMaps"
import { createObject } from "../../../Utils/apiQueries"

function CreateLabWorkSF({ onClose }: { onClose: () => void}) {
    const [name, setName] = useState("")
    const [coordinates, setCoordinates] = useState(-1) 
    const [creationDate, setCreationDate]: [any, React.Dispatch<any>] = useState("")
    const [description, setDescription] = useState("")
    const [difficulty, setDifficulty]: [any, React.Dispatch<any>] = useState("")
    const [discipline, setDiscipline] = useState(-1)
    const [minimalPoint, setMinimalPoint] = useState("")
    const [maximumPoint, setMaximumPoint] = useState("")
    const [author, setAuthor] = useState(-1)
    
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    useEffect(() => {
        let res : string[] = []
        processStringMaxLength(name, 400, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processObjectChosen(coordinates, "Координата", res)
        setErrorMsg(res)
        processIsNotNull(description, "Описание", res)
        processIsValidFloat(minimalPoint, false, "Минимальная оценка", res)
        processIsValidFloat(minimalPoint, true, "Максимальная оценка", res)
    }, [name, coordinates, creationDate, description, difficulty, discipline, minimalPoint, maximumPoint, author]);

    function createQuery(){
        let form = new URLSearchParams()
        form.append('name', name)
        form.append('coordinate_id', coordinates.toString())
        if(creationDate != "") form.append('creation_date', Date.parse(creationDate).toString())   
        form.append('description', description) 
        if(difficulty != -1) form.append('difficulty_id', difficulty)  
        if(discipline != -1) form.append('discipline_id', discipline.toString()) 
        form.append('minimal_point', minimalPoint)  
        if(maximumPoint != "") form.append('maximal_point', maximumPoint)
        if(author != -1) form.append('author_id', author.toString())      
        createObject(makeQueryCreate("LabWork"),form)
        onClose()
    }

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Создание LabWork</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={coordinates} setValue={setCoordinates} required={true} type="chooseCoordinate" field="Координата" />
                    <ParameterField value={creationDate} setValue={setCreationDate} required={false} type="dateTime" field="Дата создания" />
                    <ParameterField value={description} setValue={setDescription} required={true} type="text" field="Описание" />
                    <ParameterField value={difficulty} setValue={setDifficulty} required={false} type="enumSelectdifficulty" field="Сложность" />
                    <ParameterField value={discipline} setValue={setDiscipline} required={false} type="chooseDiscipline" field="Дисциплина" />
                    <ParameterField value={minimalPoint} setValue={setMinimalPoint} required={true} type="floatNum" field="Минимальная оценка" />
                    <ParameterField value={maximumPoint} setValue={setMaximumPoint} required={false} type="floatNum" field="Максимальная оценка" />
                    <ParameterField value={author} setValue={setAuthor} required={false} type="choosePerson" field="Автор" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    Создать
                </button>
            </div>
        </>
    )
}

export default CreateLabWorkSF