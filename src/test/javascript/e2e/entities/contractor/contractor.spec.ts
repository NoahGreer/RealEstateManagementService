import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ContractorComponentsPage, ContractorDeleteDialog, ContractorUpdatePage } from './contractor.page-object';

const expect = chai.expect;

describe('Contractor e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contractorComponentsPage: ContractorComponentsPage;
  let contractorUpdatePage: ContractorUpdatePage;
  let contractorDeleteDialog: ContractorDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Contractors', async () => {
    await navBarPage.goToEntity('contractor');
    contractorComponentsPage = new ContractorComponentsPage();
    await browser.wait(ec.visibilityOf(contractorComponentsPage.title), 5000);
    expect(await contractorComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.contractor.home.title');
    await browser.wait(ec.or(ec.visibilityOf(contractorComponentsPage.entities), ec.visibilityOf(contractorComponentsPage.noResult)), 1000);
  });

  it('should load create Contractor page', async () => {
    await contractorComponentsPage.clickOnCreateButton();
    contractorUpdatePage = new ContractorUpdatePage();
    expect(await contractorUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.contractor.home.createOrEditLabel');
    await contractorUpdatePage.cancel();
  });

  it('should create and save Contractors', async () => {
    const nbButtonsBeforeCreate = await contractorComponentsPage.countDeleteButtons();

    await contractorComponentsPage.clickOnCreateButton();

    await promise.all([
      contractorUpdatePage.setCompanyNameInput('companyName'),
      contractorUpdatePage.setStreetAddressInput('streetAddress'),
      contractorUpdatePage.setCityInput('city'),
      contractorUpdatePage.setStateCodeInput('stateCode'),
      contractorUpdatePage.setZipCodeInput('zipCode'),
      contractorUpdatePage.setPhoneNumberInput('phoneNumber'),
      contractorUpdatePage.setContactPersonInput('contactPerson'),
      contractorUpdatePage.setRatingOfWorkInput('5'),
      contractorUpdatePage.setRatingOfResponsivenessInput('5')
      // contractorUpdatePage.jobTypeSelectLastOption(),
    ]);

    expect(await contractorUpdatePage.getCompanyNameInput()).to.eq('companyName', 'Expected CompanyName value to be equals to companyName');
    expect(await contractorUpdatePage.getStreetAddressInput()).to.eq(
      'streetAddress',
      'Expected StreetAddress value to be equals to streetAddress'
    );
    expect(await contractorUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await contractorUpdatePage.getStateCodeInput()).to.eq('stateCode', 'Expected StateCode value to be equals to stateCode');
    expect(await contractorUpdatePage.getZipCodeInput()).to.eq('zipCode', 'Expected ZipCode value to be equals to zipCode');
    expect(await contractorUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await contractorUpdatePage.getContactPersonInput()).to.eq(
      'contactPerson',
      'Expected ContactPerson value to be equals to contactPerson'
    );
    expect(await contractorUpdatePage.getRatingOfWorkInput()).to.eq('5', 'Expected ratingOfWork value to be equals to 5');
    expect(await contractorUpdatePage.getRatingOfResponsivenessInput()).to.eq(
      '5',
      'Expected ratingOfResponsiveness value to be equals to 5'
    );

    await contractorUpdatePage.save();
    expect(await contractorUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contractorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Contractor', async () => {
    const nbButtonsBeforeDelete = await contractorComponentsPage.countDeleteButtons();
    await contractorComponentsPage.clickOnLastDeleteButton();

    contractorDeleteDialog = new ContractorDeleteDialog();
    expect(await contractorDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.contractor.delete.question');
    await contractorDeleteDialog.clickOnConfirmButton();

    expect(await contractorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
